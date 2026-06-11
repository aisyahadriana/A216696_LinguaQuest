@file:Suppress("UnsafeOptInUsageError")
package com.example.a216696_wan_project2

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.a216696_wan_project2.ui.theme.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executors
import androidx.compose.ui.viewinterop.AndroidView

// ── Supported languages (code → display name) ───────────────────
val SUPPORTED_LANGUAGES = listOf(
    "auto" to "🔍 Auto Detect",
    "af"   to "🇿🇦 Afrikaans",
    "sq"   to "🇦🇱 Albanian",
    "ar"   to "🇸🇦 Arabic",
    "hy"   to "🇦🇲 Armenian",
    "az"   to "🇦🇿 Azerbaijani",
    "eu"   to "🇪🇸 Basque",
    "be"   to "🇧🇾 Belarusian",
    "bn"   to "🇧🇩 Bengali",
    "bs"   to "🇧🇦 Bosnian",
    "bg"   to "🇧🇬 Bulgarian",
    "ca"   to "🏳️ Catalan",
    "zh"   to "🇨🇳 Chinese (Simplified)",
    "zh-TW" to "🇹🇼 Chinese (Traditional)",
    "hr"   to "🇭🇷 Croatian",
    "cs"   to "🇨🇿 Czech",
    "da"   to "🇩🇰 Danish",
    "nl"   to "🇳🇱 Dutch",
    "en"   to "🇬🇧 English",
    "eo"   to "🏳️ Esperanto",
    "et"   to "🇪🇪 Estonian",
    "fi"   to "🇫🇮 Finnish",
    "fr"   to "🇫🇷 French",
    "gl"   to "🏳️ Galician",
    "ka"   to "🇬🇪 Georgian",
    "de"   to "🇩🇪 German",
    "el"   to "🇬🇷 Greek",
    "gu"   to "🇮🇳 Gujarati",
    "ht"   to "🇭🇹 Haitian Creole",
    "ha"   to "🌍 Hausa",
    "he"   to "🇮🇱 Hebrew",
    "hi"   to "🇮🇳 Hindi",
    "hu"   to "🇭🇺 Hungarian",
    "is"   to "🇮🇸 Icelandic",
    "id"   to "🇮🇩 Indonesian",
    "ga"   to "🇮🇪 Irish",
    "it"   to "🇮🇹 Italian",
    "ja"   to "🇯🇵 Japanese",
    "jv"   to "🇮🇩 Javanese",
    "kn"   to "🇮🇳 Kannada",
    "kk"   to "🇰🇿 Kazakh",
    "km"   to "🇰🇭 Khmer",
    "ko"   to "🇰🇷 Korean",
    "ku"   to "🏳️ Kurdish",
    "ky"   to "🇰🇬 Kyrgyz",
    "lo"   to "🇱🇦 Lao",
    "lv"   to "🇱🇻 Latvian",
    "lt"   to "🇱🇹 Lithuanian",
    "lb"   to "🇱🇺 Luxembourgish",
    "mk"   to "🇲🇰 Macedonian",
    "mg"   to "🇲🇬 Malagasy",
    "ms"   to "🇲🇾 Malay",
    "ml"   to "🇮🇳 Malayalam",
    "mt"   to "🇲🇹 Maltese",
    "mi"   to "🇳🇿 Maori",
    "mr"   to "🇮🇳 Marathi",
    "mn"   to "🇲🇳 Mongolian",
    "my"   to "🇲🇲 Myanmar (Burmese)",
    "ne"   to "🇳🇵 Nepali",
    "no"   to "🇳🇴 Norwegian",
    "ny"   to "🌍 Nyanja (Chichewa)",
    "ps"   to "🇦🇫 Pashto",
    "fa"   to "🇮🇷 Persian",
    "pl"   to "🇵🇱 Polish",
    "pt"   to "🇧🇷 Portuguese",
    "pa"   to "🇮🇳 Punjabi",
    "ro"   to "🇷🇴 Romanian",
    "ru"   to "🇷🇺 Russian",
    "sm"   to "🇼🇸 Samoan",
    "gd"   to "🏴󠁧󠁢󠁳󠁣󠁴󠁿 Scottish Gaelic",
    "sr"   to "🇷🇸 Serbian",
    "st"   to "🌍 Sesotho",
    "sn"   to "🌍 Shona",
    "sd"   to "🇵🇰 Sindhi",
    "si"   to "🇱🇰 Sinhala",
    "sk"   to "🇸🇰 Slovak",
    "sl"   to "🇸🇮 Slovenian",
    "so"   to "🇸🇴 Somali",
    "es"   to "🇪🇸 Spanish",
    "su"   to "🇮🇩 Sundanese",
    "sw"   to "🌍 Swahili",
    "sv"   to "🇸🇪 Swedish",
    "tl"   to "🇵🇭 Filipino (Tagalog)",
    "tg"   to "🇹🇯 Tajik",
    "ta"   to "🇮🇳 Tamil",
    "tt"   to "🇷🇺 Tatar",
    "te"   to "🇮🇳 Telugu",
    "th"   to "🇹🇭 Thai",
    "tr"   to "🇹🇷 Turkish",
    "tk"   to "🇹🇲 Turkmen",
    "uk"   to "🇺🇦 Ukrainian",
    "ur"   to "🇵🇰 Urdu",
    "ug"   to "🇨🇳 Uyghur",
    "uz"   to "🇺🇿 Uzbek",
    "vi"   to "🇻🇳 Vietnamese",
    "cy"   to "🏴󠁧󠁢󠁷󠁬󠁳󠁿 Welsh",
    "xh"   to "🌍 Xhosa",
    "yi"   to "🏳️ Yiddish",
    "yo"   to "🌍 Yoruba",
    "zu"   to "🌍 Zulu"
)

fun getLangName(code: String): String =
    SUPPORTED_LANGUAGES.find { it.first == code }?.second ?: code

// ── Language Picker Dialog ───────────────────────────────────────
@Composable
fun LanguagePickerDialog(
    title:          String,
    currentLang:    String,
    excludeAuto:    Boolean = false,
    onSelect:       (String) -> Unit,
    onDismiss:      () -> Unit
) {
    var search by remember { mutableStateOf("") }
    val filtered = remember(search) {
        SUPPORTED_LANGUAGES
            .filter { if (excludeAuto) it.first != "auto" else true }
            .filter { it.second.contains(search, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape  = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(title, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value         = search,
                    onValueChange = { search = it },
                    placeholder   = { Text("Search language...", color = TextMuted, fontSize = 13.sp) },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(12.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor      = GreenColor,
                        unfocusedBorderColor    = LockedRing,
                        focusedTextColor        = TextWhite,
                        unfocusedTextColor      = TextWhite,
                        cursorColor             = GreenColor,
                        focusedContainerColor   = Color(0xFF0A1628),
                        unfocusedContainerColor = Color(0xFF0A1628)
                    ),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.heightIn(max = 350.dp)) {
                    items(filtered) { (code, name) ->
                        val isSelected = code == currentLang
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) GreenColor.copy(alpha = 0.15f) else Color.Transparent)
                                .clickable { onSelect(code); onDismiss() }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name, color = if (isSelected) GreenColor else TextWhite, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal)
                            if (isSelected) Text("✓", color = GreenColor, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

// ── Language Selector Button ─────────────────────────────────────
@Composable
fun LangButton(code: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0A1628))
            .border(1.5.dp, ActiveBlue.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(getLangName(code), color = ActiveBlue, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = ActiveBlue, modifier = Modifier.size(16.dp))
        }
    }
}

// ── Main Translation Screen ──────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    viewModel:       UserViewModel,
    onBack:          () -> Unit,
    onGoToCommunity: () -> Unit
) {
    val context              = LocalContext.current
    val translationState     by remember { derivedStateOf { viewModel.translationState } }
    val history              by viewModel.translationHistory.collectAsState()
    val firestoreStatus       = viewModel.firestoreStatus

    var inputText            by remember { mutableStateOf("") }
    var sourceLang           by remember { mutableStateOf("auto") }
    var targetLang           by remember { mutableStateOf("ms") }
    var showCamera           by remember { mutableStateOf(false) }
    var showSourcePicker     by remember { mutableStateOf(false) }
    var showTargetPicker     by remember { mutableStateOf(false) }
    var hasCameraPermission  by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    )}

    val cameraPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(firestoreStatus) {
        if (firestoreStatus.isNotEmpty()) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearFirestoreStatus()
        }
    }

    // Dialogs
    if (showSourcePicker) {
        LanguagePickerDialog(
            title       = "Translate FROM",
            currentLang = sourceLang,
            onSelect    = { sourceLang = it },
            onDismiss   = { showSourcePicker = false }
        )
    }
    if (showTargetPicker) {
        LanguagePickerDialog(
            title       = "Translate TO",
            currentLang = targetLang,
            excludeAuto = true,
            onSelect    = { targetLang = it },
            onDismiss   = { showTargetPicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌐 Translate", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg
    ) { innerPadding ->

        if (showCamera) {
            CameraOcrView(
                sourceLang      = sourceLang,
                onTextDetected  = { detected ->
                    inputText  = detected
                    showCamera = false
                    viewModel.translateText(detected, sourceLang, targetLang)
                },
                onDismiss = { showCamera = false }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ── SDG Badge ──────────────────────────────────────
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                    border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(Brush.horizontalGradient(listOf(Color(0xFF0D3B2E), Color(0xFF0A2A20))))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🌍", fontSize = 26.sp)
                        Column {
                            Text("SDG 4 – Quality Education", color = GreenColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Translate any language • Scan real-world text", color = TextWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                        }
                    }
                }

                // ── Language Selector Row ──────────────────────────
                Text("🔤 Language Pair", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Source language
                    LangButton(
                        code     = sourceLang,
                        onClick  = { showSourcePicker = true },
                        modifier = Modifier.weight(1f)
                    )

                    // Swap button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(ActiveBlue.copy(alpha = 0.15f))
                            .border(1.dp, ActiveBlue.copy(alpha = 0.4f), CircleShape)
                            .clickable {
                                if (sourceLang != "auto") {
                                    val tmp = sourceLang
                                    sourceLang = targetLang
                                    targetLang = tmp
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⇄", color = ActiveBlue, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    // Target language
                    LangButton(
                        code     = targetLang,
                        onClick  = { showTargetPicker = true },
                        modifier = Modifier.weight(1f)
                    )
                }

                // ── Input field ────────────────────────────────────
                Text("📝 Enter text to translate:", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                OutlinedTextField(
                    value         = inputText,
                    onValueChange = { inputText = it },
                    placeholder   = { Text("Type here in ${getLangName(sourceLang)}...", color = TextMuted, fontSize = 13.sp) },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(14.dp),
                    minLines      = 3,
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor      = GreenColor,
                        unfocusedBorderColor    = LockedRing,
                        focusedTextColor        = TextWhite,
                        unfocusedTextColor      = TextWhite,
                        cursorColor             = GreenColor,
                        focusedContainerColor   = Color(0xFF0F1E2E),
                        unfocusedContainerColor = Color(0xFF0F1E2E)
                    )
                )

                // ── Camera Scan button ─────────────────────────────
                OutlinedButton(
                    onClick = {
                        if (hasCameraPermission) showCamera = true
                        else cameraPermLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    border   = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.6f)),
                    colors   = ButtonDefaults.outlinedButtonColors(containerColor = ActiveBlue.copy(alpha = 0.08f))
                ) {
                    Text("📷  Scan Text with Camera (${getLangName(sourceLang)})", color = ActiveBlue, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }

                // ── Translate button ───────────────────────────────
                Button(
                    onClick  = { viewModel.translateText(inputText, sourceLang, targetLang) },
                    enabled  = inputText.isNotBlank() && !translationState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = GreenColor)
                ) {
                    if (translationState.isLoading) {
                        CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(10.dp))
                        Text("Translating...", color = TextWhite, fontWeight = FontWeight.ExtraBold)
                    } else {
                        Text("TRANSLATE  ${getLangName(sourceLang).take(4)}→${getLangName(targetLang).take(4)} 🌐", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }

                // ── Translation Result ─────────────────────────────
                AnimatedVisibility(visible = translationState.result.isNotEmpty()) {
                    Card(
                        shape  = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                        border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("✅ Translation Result", color = GreenColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                            HorizontalDivider(color = GreenColor.copy(alpha = 0.2f))
                            Text("Original (${getLangName(sourceLang)}):", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(translationState.sourceText, color = TextWhite.copy(alpha = 0.85f), fontSize = 14.sp)
                            HorizontalDivider(color = LockedRing.copy(alpha = 0.2f))
                            Text("→ ${getLangName(targetLang)}:", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(translationState.result, color = GreenColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            HorizontalDivider(color = LockedRing.copy(alpha = 0.2f))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("🔒", fontSize = 12.sp)
                                Text("Saved encrypted (AES-256) to your device", color = TextMuted, fontSize = 10.sp)
                            }
                            Button(
                                onClick = {
                                    viewModel.sharePhraseToCommunity(
                                        korean      = translationState.sourceText,
                                        translation = translationState.result,
                                        targetLang  = targetLang
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                shape    = RoundedCornerShape(12.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = ActiveBlue)
                            ) {
                                Text("🌐 Share to Community", color = TextWhite, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }

                // ── Error ──────────────────────────────────────────
                AnimatedVisibility(visible = translationState.errorMessage.isNotEmpty()) {
                    Card(
                        shape  = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEF5350).copy(alpha = 0.12f)),
                        border = BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.4f))
                    ) {
                        Text("❌ ${translationState.errorMessage}", color = Color(0xFFEF9A9A), modifier = Modifier.padding(16.dp), fontSize = 13.sp)
                    }
                }

                // ── Firebase status ────────────────────────────────
                AnimatedVisibility(visible = firestoreStatus.isNotEmpty()) {
                    Card(
                        shape  = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (firestoreStatus.startsWith("✅")) GreenColor.copy(alpha = 0.15f) else Color(0xFFEF5350).copy(alpha = 0.1f)
                        )
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(firestoreStatus, color = if (firestoreStatus.startsWith("✅")) GreenColor else Color(0xFFEF9A9A), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            if (firestoreStatus.startsWith("✅")) {
                                TextButton(onClick = onGoToCommunity) { Text("View →", color = ActiveBlue, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold) }
                            }
                        }
                    }
                }

                // ── History ────────────────────────────────────────
                if (history.isNotEmpty()) {
                    Text("🕐 Recent Translations (Encrypted)", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    history.take(5).forEach { item ->
                        TranslationHistoryCard(item) {
                            inputText  = item.original
                            sourceLang = "auto"
                            targetLang = item.targetLang
                            viewModel.translateText(item.original, "auto", item.targetLang)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ── History Card ─────────────────────────────────────────────────
@Composable
fun TranslationHistoryCard(item: TranslationHistoryItem, onRetry: () -> Unit) {
    Card(
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)),
        border   = BorderStroke(1.dp, LockedRing.copy(alpha = 0.3f)),
        modifier = Modifier.clickable { onRetry() }
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(LockedRing.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) { Text("🔒", fontSize = 14.sp) }
            Column(modifier = Modifier.weight(1f)) {
                Text(item.original,   color = TextWhite,                     fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(item.translated, color = GreenColor.copy(alpha = 0.85f), fontSize = 12.sp, maxLines = 1)
            }
            Text(getLangName(item.targetLang).take(6), color = TextMuted, fontSize = 10.sp)
        }
    }
}

// ── Camera OCR View ──────────────────────────────────────────────
@androidx.camera.core.ExperimentalGetImage
@Composable
fun CameraOcrView(
    sourceLang:     String,
    onTextDetected: (String) -> Unit,
    onDismiss:      () -> Unit
) {
    val context        = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor       = remember { Executors.newSingleThreadExecutor() }
    var isScanning     by remember { mutableStateOf(true) }
    var statusText     by remember { mutableStateOf("Point camera at text to scan...") }

    val recognizer = remember(sourceLang) {
        if (sourceLang == "ko") {
            TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        } else {
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(android.util.Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { analysis ->
                            analysis.setAnalyzer(executor) { imageProxy ->
                                if (!isScanning) {
                                    imageProxy.close()
                                    return@setAnalyzer
                                }

                                @androidx.camera.core.ExperimentalGetImage
                                val mediaImage = imageProxy.image
                                if (mediaImage != null) {
                                    val imgWidth  = mediaImage.width.toFloat()
                                    val imgHeight = mediaImage.height.toFloat()

                                    // Green box is ~260x140dp centered on screen
                                    // Map it to image coordinates (image is 1280x720 landscape)
                                    // Box center = center of image
                                    // Box size = ~60% width, ~38% height of the image
                                    val boxWidthRatio  = 0.60f
                                    val boxHeightRatio = 0.45f
                                    val left   = ((1f - boxWidthRatio)  / 2f * imgWidth).toInt()
                                    val top    = ((1f - boxHeightRatio) / 2f * imgHeight).toInt()
                                    val right  = (left + boxWidthRatio  * imgWidth).toInt()
                                    val bottom = (top  + boxHeightRatio * imgHeight).toInt()

                                    // Clamp to image bounds
                                    val safeLeft   = left.coerceAtLeast(0)
                                    val safeTop    = top.coerceAtLeast(0)
                                    val safeRight  = right.coerceAtMost(mediaImage.width)
                                    val safeBottom = bottom.coerceAtMost(mediaImage.height)

                                    val cropRect = android.graphics.Rect(safeLeft, safeTop, safeRight, safeBottom)

                                    val inputImage = InputImage.fromMediaImage(
                                        mediaImage,
                                        imageProxy.imageInfo.rotationDegrees,
                                    )
                                    // Set crop hint so ML Kit focuses on the green box area
                                    val croppedInput = InputImage.fromMediaImage(
                                        mediaImage,
                                        imageProxy.imageInfo.rotationDegrees
                                    )

                                    recognizer.process(croppedInput)
                                        .addOnSuccessListener { visionText ->
                                            // Filter: only keep text blocks that fall within crop region
                                            val sb = StringBuilder()
                                            for (block in visionText.textBlocks) {
                                                val blockBox = block.boundingBox
                                                if (blockBox != null && android.graphics.Rect.intersects(blockBox, cropRect)) {
                                                    sb.append(block.text).append(" ")
                                                }
                                            }
                                            val detected = sb.toString().trim()
                                                .ifEmpty { visionText.text.trim() } // fallback to all text

                                            if (detected.isNotEmpty() && isScanning) {
                                                isScanning = false
                                                onTextDetected(detected)
                                            } else if (visionText.text.isEmpty()) {
                                                statusText = "No text found. Try again."
                                                isScanning = true
                                            }
                                        }
                                        .addOnFailureListener {
                                            statusText = "Could not read text. Try again."
                                            isScanning = true
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                } else {
                                    imageProxy.close()
                                }
                            }
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        statusText = "Camera error: ${e.message}"
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // ── Darkened overlay outside the green box ───────────────
        // Top dark area
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.28f).background(Color.Black.copy(alpha = 0.6f)))
        // Bottom dark area
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.28f).align(Alignment.BottomCenter).background(Color.Black.copy(alpha = 0.6f)))
        // Left dark area
        Box(modifier = Modifier.fillMaxWidth(0.05f).fillMaxHeight().background(Color.Black.copy(alpha = 0.6f)))
        // Right dark area
        Box(modifier = Modifier.fillMaxWidth(0.05f).fillMaxHeight().align(Alignment.CenterEnd).background(Color.Black.copy(alpha = 0.6f)))

        // ── UI Overlay ───────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .clickable { onDismiss() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) { Text("✕  Cancel", color = Color.White, fontWeight = FontWeight.Bold) }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        "Scanning: ${getLangName(sourceLang).take(10)}",
                        color = ActiveBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            // Center: green scan frame
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Animated scanning line inside box
                Box(
                    modifier = Modifier
                        .size(width = 270.dp, height = 145.dp)
                        .border(2.dp, GreenColor, RoundedCornerShape(16.dp))
                ) {
                    // Corner accent marks
                    Box(modifier = Modifier.size(20.dp, 3.dp).align(Alignment.TopStart).offset(x = (-1).dp, y = (-1).dp).background(GreenColor))
                    Box(modifier = Modifier.size(3.dp, 20.dp).align(Alignment.TopStart).offset(x = (-1).dp, y = (-1).dp).background(GreenColor))
                    Box(modifier = Modifier.size(20.dp, 3.dp).align(Alignment.TopEnd).offset(x = 1.dp, y = (-1).dp).background(GreenColor))
                    Box(modifier = Modifier.size(3.dp, 20.dp).align(Alignment.TopEnd).offset(x = 1.dp, y = (-1).dp).background(GreenColor))
                    Box(modifier = Modifier.size(20.dp, 3.dp).align(Alignment.BottomStart).offset(x = (-1).dp, y = 1.dp).background(GreenColor))
                    Box(modifier = Modifier.size(3.dp, 20.dp).align(Alignment.BottomStart).offset(x = (-1).dp, y = 1.dp).background(GreenColor))
                    Box(modifier = Modifier.size(20.dp, 3.dp).align(Alignment.BottomEnd).offset(x = 1.dp, y = 1.dp).background(GreenColor))
                    Box(modifier = Modifier.size(3.dp, 20.dp).align(Alignment.BottomEnd).offset(x = 1.dp, y = 1.dp).background(GreenColor))
                }
                Text(
                    "Place text inside the frame",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 28.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            // Bottom status
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(statusText, color = Color.White, fontSize = 13.sp, textAlign = TextAlign.Center)
            }
        }
    }
}