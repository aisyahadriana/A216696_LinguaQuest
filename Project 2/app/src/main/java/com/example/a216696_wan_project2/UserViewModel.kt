package com.example.a216696_wan_project2

// ================================================================
// FILE: UserViewModel.kt  (UPDATED v3)
// Changes:
//   - Fixed translateText() to use new TranslateRetrofitClient.translate()
//   - Lesson vocabulary dynamically translated via Google Translate API
//   - No hardcoded translations — API fetches them on language switch
// ================================================================

import android.app.Application
import android.content.SharedPreferences
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.crypto.SecretKey

data class TranslationState(
    val isLoading:    Boolean = false,
    val result:       String  = "",
    val errorMessage: String  = "",
    val sourceText:   String  = "",
    val sourceLang:   String  = "auto",
    val targetLang:   String  = "ms"
)

data class TranslationHistoryItem(
    val original:    String,
    val translated:  String,
    val sourceLang:  String = "auto",
    val targetLang:  String,
    val timestamp:   Long = System.currentTimeMillis()
)

data class UserData(
    val userName:      String = "",
    val currentLesson: String = ""
)

// ── Language definition (base English words, translated dynamically) ──
data class LessonLanguage(
    val code:        String,
    val displayName: String,
    val nativeName:  String,
    val flag:        String,
    val greeting:    String,
    // sampleWords: English word → translated word (fetched via API)
    val sampleWords: List<Pair<String, String>> = emptyList()
)

// Base languages — greeting and words translated dynamically via API
val LESSON_LANGUAGES = listOf(
    LessonLanguage("ko", "Korean",   "한국어",        "🇰🇷", "안녕하세요"),
    LessonLanguage("ms", "Malay",    "Bahasa Melayu", "🇲🇾", "Selamat datang"),
    LessonLanguage("ja", "Japanese", "日本語",        "🇯🇵", "こんにちは"),
    LessonLanguage("zh", "Chinese",  "中文",          "🇨🇳", "你好"),
    LessonLanguage("fr", "French",   "Français",      "🇫🇷", "Bonjour"),
    LessonLanguage("ar", "Arabic",   "العربية",       "🇸🇦", "مرحبا"),
    LessonLanguage("es", "Spanish",  "Español",       "🇪🇸", "Hola"),
    LessonLanguage("de", "German",   "Deutsch",       "🇩🇪", "Hallo"),
)

// Base English words to translate for lesson vocabulary
val BASE_LESSON_WORDS = listOf(
    "Airport", "Where is it?", "Ticket", "Thank you", "Please", "Hello", "Goodbye", "Help"
)

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository:          AppRepository
    private val firestoreRepository: FirestoreRepository = FirestoreRepository()
    private val encryptedPrefs:      SharedPreferences =
        EncryptionManager.getEncryptedPrefs(application)
    private val sessionKey: SecretKey

    // ── App-wide lesson language (declared BEFORE init) ───────────
    private var _appLessonLanguage by mutableStateOf(LESSON_LANGUAGES[0])
    var appLessonLanguage: LessonLanguage
        get() = _appLessonLanguage
        private set(value) { _appLessonLanguage = value }

    // ── Dynamic vocabulary from API ───────────────────────────────
    var lessonVocabulary by mutableStateOf<List<Pair<String, String>>>(emptyList())
        private set
    var isLoadingVocab by mutableStateOf(false)
        private set

    fun setLessonLanguage(lang: LessonLanguage) {
        _appLessonLanguage = lang
        encryptedPrefs.edit().putString("lesson_lang", lang.code).apply()
        // Fetch vocabulary dynamically via Google Translate API
        fetchLessonVocabulary(lang.code)
    }

    // Translate BASE_LESSON_WORDS into the selected language via API
    private fun fetchLessonVocabulary(langCode: String) {
        if (langCode == "en") {
            lessonVocabulary = BASE_LESSON_WORDS.map { it to it }
            return
        }
        isLoadingVocab = true
        lessonVocabulary = emptyList()
        viewModelScope.launch {
            val translated = mutableListOf<Pair<String, String>>()
            for (word in BASE_LESSON_WORDS) {
                try {
                    val result = TranslateRetrofitClient.translate(
                        sl = "en",
                        tl = langCode,
                        q  = word
                    )
                    translated.add(word to (if (result.isNotEmpty()) result else word))
                } catch (e: Exception) {
                    translated.add(word to word) // fallback to English
                }
            }
            lessonVocabulary = translated
            isLoadingVocab   = false
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository   = AppRepository(database.goalDao())

        val storedKeyB64 = encryptedPrefs.getString("aes_session_key", null)
        sessionKey = if (storedKeyB64 != null) {
            EncryptionManager.keyFromBytes(Base64.decode(storedKeyB64, Base64.NO_WRAP))
        } else {
            val newKey = EncryptionManager.generateKey()
            encryptedPrefs.edit().putString("aes_session_key",
                Base64.encodeToString(newKey.encoded, Base64.NO_WRAP)).apply()
            newKey
        }

        val savedName = encryptedPrefs.getString("user_name", "") ?: ""
        if (savedName.isNotEmpty()) setUserName(savedName)

        val savedLangCode = encryptedPrefs.getString("lesson_lang", "ko") ?: "ko"
        val savedLang = LESSON_LANGUAGES.find { it.code == savedLangCode } ?: LESSON_LANGUAGES[0]
        _appLessonLanguage = savedLang
        fetchLessonVocabulary(savedLang.code)

        loadEncryptedHistory()
    }

    // ── User data ─────────────────────────────────────────────────
    private var _userData = UserData()
    var userData: UserData
        get() = _userData
        private set(value) { _userData = value }

    fun setUserName(name: String) {
        _userData = _userData.copy(userName = name)
        encryptedPrefs.edit().putString("user_name", name).apply()
    }

    fun setCurrentLesson(lesson: String) {
        _userData = _userData.copy(currentLesson = lesson)
    }

    // ── Room Goals ────────────────────────────────────────────────
    val studyGoals: StateFlow<List<GoalEntity>> = repository.allGoals.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addGoal(title: String, targetDays: Int) {
        viewModelScope.launch {
            repository.insertGoal(GoalEntity(title = title, targetDays = targetDays))
        }
    }

    fun toggleGoal(id: Int, currentStatus: Boolean) {
        viewModelScope.launch { repository.toggleGoal(id, !currentStatus) }
    }

    fun deleteGoal(id: Int) {
        viewModelScope.launch { repository.deleteGoal(id) }
    }

    fun resetProgress() {
        _userData = UserData()
        encryptedPrefs.edit().remove("user_name").apply()
        viewModelScope.launch { repository.deleteAllGoals() }
        _translationHistory.value = emptyList()
        clearEncryptedHistory()
    }

    // ── Translation ───────────────────────────────────────────────
    var translationState by mutableStateOf(TranslationState())
        private set

    private val _translationHistory = MutableStateFlow<List<TranslationHistoryItem>>(emptyList())
    val translationHistory: StateFlow<List<TranslationHistoryItem>> = _translationHistory

    fun translateText(text: String, sourceLang: String = "auto", targetLang: String = "ms") {
        if (text.isBlank()) return
        translationState = translationState.copy(
            isLoading    = true,
            result       = "",
            errorMessage = "",
            sourceText   = text,
            sourceLang   = sourceLang,
            targetLang   = targetLang
        )
        viewModelScope.launch {
            try {
                val translated = TranslateRetrofitClient.translate(
                    sl = sourceLang,
                    tl = targetLang,
                    q  = text
                )
                if (translated.isNotEmpty()) {
                    translationState = translationState.copy(
                        isLoading = false,
                        result    = translated
                    )
                    saveToEncryptedHistory(TranslationHistoryItem(
                        original   = text,
                        translated = translated,
                        sourceLang = sourceLang,
                        targetLang = targetLang
                    ))
                } else {
                    translationState = translationState.copy(
                        isLoading    = false,
                        errorMessage = "Translation not available. Try again."
                    )
                }
            } catch (e: Exception) {
                translationState = translationState.copy(
                    isLoading    = false,
                    errorMessage = "Network error: ${e.localizedMessage}"
                )
            }
        }
    }

    fun clearTranslation() { translationState = TranslationState() }

    // ── Encrypted history ─────────────────────────────────────────
    private fun saveToEncryptedHistory(item: TranslationHistoryItem) {
        val current = _translationHistory.value.toMutableList()
        current.add(0, item)
        if (current.size > 20) current.removeLastOrNull()
        _translationHistory.value = current
        val serialized = current.joinToString("|SPLIT|") { h ->
            val raw = "${h.original}|||${h.translated}|||${h.sourceLang}|||${h.targetLang}|||${h.timestamp}"
            EncryptionManager.encrypt(raw, sessionKey)
        }
        encryptedPrefs.edit().putString("translation_history", serialized).apply()
    }

    private fun loadEncryptedHistory() {
        val stored = encryptedPrefs.getString("translation_history", null) ?: return
        try {
            val items = stored.split("|SPLIT|").mapNotNull { encrypted ->
                val decrypted = EncryptionManager.decrypt(encrypted, sessionKey)
                val parts     = decrypted.split("|||")
                if (parts.size >= 4) {
                    TranslationHistoryItem(
                        original   = parts[0],
                        translated = parts[1],
                        sourceLang = if (parts.size >= 5) parts[2] else "auto",
                        targetLang = if (parts.size >= 5) parts[3] else parts[2],
                        timestamp  = parts.lastOrNull()?.toLongOrNull() ?: 0L
                    )
                } else null
            }
            _translationHistory.value = items
        } catch (e: Exception) { clearEncryptedHistory() }
    }

    private fun clearEncryptedHistory() {
        encryptedPrefs.edit().remove("translation_history").apply()
    }

    // ── Firebase Community ────────────────────────────────────────
    val communityPhrases: StateFlow<List<CommunityPhrase>> =
        firestoreRepository.getCommunityPhrasesFlow()
            .stateIn(
                scope        = viewModelScope,
                started      = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    var firestoreStatus by mutableStateOf("")
        private set

    fun sharePhraseToCommunity(korean: String, translation: String, targetLang: String) {
        viewModelScope.launch {
            firestoreStatus = "Sharing..."
            val phrase = CommunityPhrase(
                korean      = korean,
                translation = translation,
                submittedBy = _userData.userName.ifBlank { "Anonymous" },
                targetLang  = targetLang
            )
            val result = firestoreRepository.addPhrase(phrase)
            firestoreStatus = if (result.isSuccess) "✅ Shared to community!" else "❌ Failed to share."
        }
    }

    fun likePhrase(docId: String) {
        viewModelScope.launch { firestoreRepository.likePhrase(docId) }
    }

    fun clearFirestoreStatus() { firestoreStatus = "" }
}