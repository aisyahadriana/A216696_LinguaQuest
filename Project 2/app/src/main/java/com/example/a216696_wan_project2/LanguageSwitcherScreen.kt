package com.example.a216696_wan_project2

// ================================================================
// FILE: LanguageSwitcherScreen.kt  (UPDATED)
// LessonDetailScreen now shows vocabulary fetched via Google Translate API
// LanguageSwitcherScreen unchanged
// ================================================================

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216696_wan_project2.ui.theme.*

// ── Language Switcher Screen ─────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSwitcherScreen(
    viewModel: UserViewModel,
    onBack:    () -> Unit
) {
    val current = viewModel.appLessonLanguage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌍 Choose Learning Language", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Info card
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D3B2E)),
                border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.4f))
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("🎓", fontSize = 28.sp)
                    Column {
                        Text("SDG 4 – Quality Education", color = GreenColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Vocabulary is translated live via Google Translate API!", color = TextWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                    }
                }
            }

            Text("Select a language to learn:", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)

            LESSON_LANGUAGES.forEach { lang ->
                val isSelected = lang.code == current.code
                Card(
                    modifier  = Modifier.fillMaxWidth().clickable { viewModel.setLessonLanguage(lang) },
                    shape     = RoundedCornerShape(18.dp),
                    colors    = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF0D3B2E) else Color(0xFF0D2235)),
                    border    = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) GreenColor else LockedRing.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Box(
                            modifier = Modifier.size(52.dp).clip(CircleShape)
                                .background(if (isSelected) GreenColor.copy(alpha = 0.2f) else LockedRing.copy(alpha = 0.1f))
                                .border(1.5.dp, if (isSelected) GreenColor else LockedRing.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text(lang.flag, fontSize = 26.sp) }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(lang.displayName, color = if (isSelected) GreenColor else TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Text(lang.nativeName,  color = TextMuted, fontSize = 12.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("\"${lang.greeting}\"", color = if (isSelected) GreenColor.copy(alpha = 0.8f) else TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        if (isSelected) {
                            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(GreenColor), contentAlignment = Alignment.Center) {
                                Text("✓", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // API note
            Card(
                shape  = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1F3B)),
                border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.3f))
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🌐", fontSize = 20.sp)
                    Text(
                        "Vocabulary words are fetched dynamically using the free Google Translate API — no hardcoded translations!",
                        color = TextWhite.copy(alpha = 0.8f), fontSize = 11.sp, lineHeight = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Lesson Detail Screen (dynamic vocab from Google Translate API) ──
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    viewModel: UserViewModel,
    onBack:    () -> Unit
) {
    val lang       = viewModel.appLessonLanguage
    val lesson     = viewModel.userData.currentLesson
    val vocabulary = viewModel.lessonVocabulary
    val isLoading  = viewModel.isLoadingVocab

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${lang.flag} ${lang.displayName} Lesson", color = TextWhite, fontWeight = FontWeight.ExtraBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language badge
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D3B2E)),
                border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFF0D3B2E), Color(0xFF0A2A20))))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(lang.flag, fontSize = 32.sp)
                    Column {
                        Text("Learning: ${lang.displayName}", color = GreenColor, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        Text(lesson.ifBlank { "Unit 1 – Basics" }, color = TextWhite.copy(alpha = 0.75f), fontSize = 12.sp)
                    }
                }
            }

            // Greeting card
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("👋 Greeting", color = ActiveBlue, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    Text(lang.greeting, color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text("= Hello / Welcome", color = TextMuted, fontSize = 13.sp)
                }
            }

            // Vocabulary — from Google Translate API
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📖 Vocabulary (${lang.displayName})", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(GreenColor.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 3.dp)
                ) { Text("via Google Translate API 🌐", color = GreenColor, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
            }

            if (isLoading) {
                Card(
                    shape  = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628))
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        CircularProgressIndicator(color = ActiveBlue, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        Text("Fetching vocabulary via Google Translate API...", color = TextMuted, fontSize = 13.sp)
                    }
                }
            } else if (vocabulary.isEmpty()) {
                Card(
                    shape  = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628))
                ) {
                    Text("Vocabulary loading... make sure you have internet connection.", color = TextMuted, modifier = Modifier.padding(16.dp), fontSize = 13.sp)
                }
            } else {
                vocabulary.forEachIndexed { index, (english, translated) ->
                    Card(
                        shape  = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)),
                        border = BorderStroke(1.dp, LockedRing.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(CircleShape).background(ActiveBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) { Text("${index + 1}", color = ActiveBlue, fontWeight = FontWeight.ExtraBold) }
                            Column(modifier = Modifier.weight(1f)) {
                                // Translated word (from API)
                                Text(translated, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                // English meaning
                                Text(english, color = TextMuted, fontSize = 12.sp)
                            }
                            Text(lang.flag, fontSize = 20.sp)
                        }
                    }
                }
            }

            // Practice tip
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1F3B)),
                border = BorderStroke(1.dp, GoldColor.copy(alpha = 0.3f))
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("💡", fontSize = 24.sp)
                    Column {
                        Text("Practice Tip", color = GoldColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Go to 🌐 Translate → type these words to practice ${lang.displayName}!", color = TextWhite.copy(alpha = 0.75f), fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}