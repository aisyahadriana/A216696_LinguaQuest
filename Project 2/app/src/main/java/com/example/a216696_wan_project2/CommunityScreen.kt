package com.example.a216696_wan_project2

// ================================================================
// FILE: CommunityScreen.kt  (NEW — Project 2, Screen 9)
// Features:
//   - Real-time Firebase Firestore listener (live updates)
//   - Displays community-shared Korean phrases + translations
//   - Like system (increments Firestore field)
//   - Shows who shared each phrase + timestamp
//   - SDG 4 framing: community learning together
// ================================================================

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216696_wan_project2.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    viewModel: UserViewModel,
    onBack:    () -> Unit
) {
    val phrases by viewModel.communityPhrases.collectAsState()
    val isLoading = phrases.isEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌐 Community Phrases", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Firebase header card ──────────────────────────────
            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border = BorderStroke(1.dp, Color(0xFF4DD0E1).copy(alpha = 0.4f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFF0A1F3B), Color(0xFF0D2235))))
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("☁️", fontSize = 30.sp)
                        Column {
                            Text("Firebase Community Board", color = Color(0xFF4DD0E1), fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Phrases shared by learners worldwide", color = TextWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                            Spacer(Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(8.dp).clip(CircleShape).background(GreenColor)
                                )
                                Text("Live • ${phrases.size} phrases", color = GreenColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // ── SDG 4 banner ──────────────────────────────────────
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D3B2E)),
                border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("🌍", fontSize = 22.sp)
                    Text(
                        "SDG 4: Learn from each other! Every shared phrase helps the community grow.",
                        color    = TextWhite.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // ── Loading / Empty state ─────────────────────────────
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        CircularProgressIndicator(color = ActiveBlue)
                        Text("Loading community phrases...", color = TextMuted, fontSize = 13.sp)
                        Text("(Connecting to Firebase Firestore)", color = TextMuted.copy(alpha = 0.5f), fontSize = 10.sp)
                    }
                }
            } else if (phrases.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("🌐", fontSize = 48.sp)
                        Text("No community phrases yet!", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Be the first! Translate something\nand tap 'Share to Community' 🚀",
                            color = TextMuted, fontSize = 13.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                // ── Phrase list ───────────────────────────────────
                Text("📚 Shared Phrases (${phrases.size})", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)

                phrases.forEach { phrase ->
                    CommunityPhraseCard(
                        phrase   = phrase,
                        onLike   = { viewModel.likePhrase(phrase.id) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Community Phrase Card ────────────────────────────────────────
@Composable
fun CommunityPhraseCard(
    phrase: CommunityPhrase,
    onLike: () -> Unit
) {
    val langFlag = if (phrase.targetLang == "ms") "🇲🇾" else "🇬🇧"
    val dateStr  = remember(phrase.timestamp) {
        SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
            .format(Date(phrase.timestamp))
    }

    Card(
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
        border    = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // ── Top row: submitter + lang + timestamp ─────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.size(32.dp).clip(CircleShape).background(ActiveBlue.copy(alpha = 0.15f)).border(1.dp, ActiveBlue.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            phrase.submittedBy.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                            color      = ActiveBlue,
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column {
                        Text(phrase.submittedBy, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(dateStr, color = TextMuted, fontSize = 10.sp)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(langFlag, fontSize = 16.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0A1628))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("Firestore ☁️", color = Color(0xFF4DD0E1), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            HorizontalDivider(color = LockedRing.copy(alpha = 0.2f))

            // ── Korean original ───────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("🇰🇷 Korean", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(phrase.korean, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }

            // ── Translation ───────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("$langFlag Translation", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(phrase.translation, color = GreenColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            HorizontalDivider(color = LockedRing.copy(alpha = 0.2f))

            // ── Like button ───────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("🔒", fontSize = 10.sp)
                    Text("Stored in Firebase Firestore", color = TextMuted.copy(alpha = 0.6f), fontSize = 9.sp)
                }
                OutlinedButton(
                    onClick = onLike,
                    modifier = Modifier.height(34.dp),
                    shape    = RoundedCornerShape(10.dp),
                    border   = BorderStroke(1.dp, GoldColor.copy(alpha = 0.5f)),
                    colors   = ButtonDefaults.outlinedButtonColors(containerColor = GoldColor.copy(alpha = 0.08f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text("❤️  ${phrase.likes}", color = GoldColor, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}