package com.example.a216696_wan_project1

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
import com.example.a216696_wan_project1.ui.theme.*

// ═══════════════════════════════════════════════════════════════
//  SCREEN 2 – LESSON DETAIL
// ═══════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(viewModel: UserViewModel, onBack: () -> Unit) {
    val userName      = viewModel.userData.userName
    val currentLesson = viewModel.userData.currentLesson

    var selectedAnswer  by remember { mutableStateOf("") }
    var answerSubmitted by remember { mutableStateOf(false) }
    val correctAnswer = "공항"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lesson", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Greeting card — reads userName from ViewModel
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.4f))) {
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Brush.horizontalGradient(listOf(Color(0xFF0A1628), Color(0xFF0D2235)))).padding(20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(if (userName.isNotEmpty()) "✈️  안녕하세요, $userName!" else "✈️  안녕하세요!", color = GreenColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("📍 Now studying: $currentLesson", color = TextWhite.copy(alpha = 0.75f), fontSize = 13.sp, lineHeight = 18.sp)
                        Spacer(Modifier.height(4.dp))
                        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(GoldColor.copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Text("⭐ +10 XP this lesson", color = GoldColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Vocabulary section
            Text("📚 Vocabulary", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            listOf(
                Triple("✈️", "공항",    "Airport"),
                Triple("🎫", "표",      "Ticket"),
                Triple("🗣️","어디에요?","Where is it?"),
                Triple("🧳", "짐",      "Luggage"),
                Triple("🚪", "출구",    "Exit / Gate")
            ).forEach { (icon, korean, english) -> VocabRow(icon, korean, english) }

            // Quiz section
            Text("❓ Quick Quiz", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.3f))) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("What is the Korean word for \"Airport\"?", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    listOf("비행기", "공항", "출구", "표").forEach { option ->
                        val isSelected = selectedAnswer == option
                        val isCorrect  = answerSubmitted && option == correctAnswer
                        val isWrong    = answerSubmitted && isSelected && option != correctAnswer
                        OutlinedButton(
                            onClick  = { if (!answerSubmitted) selectedAnswer = option },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                            border   = BorderStroke(1.5.dp, when { isCorrect -> GreenColor; isWrong -> Color(0xFFEF5350); isSelected -> ActiveBlue; else -> LockedRing.copy(alpha = 0.4f) }),
                            colors   = ButtonDefaults.outlinedButtonColors(containerColor = when { isCorrect -> GreenColor.copy(alpha = 0.12f); isWrong -> Color(0xFFEF5350).copy(alpha = 0.1f); isSelected -> ActiveBlue.copy(alpha = 0.12f); else -> Color(0xFF0F1E2E) })
                        ) { Text(option, color = TextWhite, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal) }
                    }
                    if (!answerSubmitted) {
                        Button(onClick = { if (selectedAnswer.isNotEmpty()) answerSubmitted = true }, enabled = selectedAnswer.isNotEmpty(), modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = GreenColor)) {
                            Text("CHECK ANSWER ✅", color = TextWhite, fontWeight = FontWeight.ExtraBold)
                        }
                    } else {
                        val isRight = selectedAnswer == correctAnswer
                        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = if (isRight) GreenColor.copy(alpha = 0.15f) else Color(0xFFEF5350).copy(alpha = 0.12f))) {
                            Text(if (isRight) "🎉 Correct! 공항 means Airport." else "❌ Not quite. The answer is 공항 (Airport).", color = if (isRight) GreenColor else Color(0xFFEF9A9A), modifier = Modifier.padding(16.dp), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(4.dp))
                        Button(
                            onClick  = { },
                            enabled  = true,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = ActiveBlue
                            )
                        ) { Text("📖  START FULL LESSON", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold) }
                        Text("Complete more units to unlock!", color = TextMuted, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun VocabRow(icon: String, korean: String, english: String) {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)), border = BorderStroke(1.dp, LockedRing.copy(alpha = 0.3f))) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(ActiveBlue.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) { Text(icon, fontSize = 18.sp) }
            Column {
                Text(korean,  color = TextWhite,                   fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                Text(english, color = TextWhite.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  SCREEN 5 – PROFILE
// ═══════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: UserViewModel, onBack: () -> Unit) {
    val userName      = viewModel.userData.userName
    val currentLesson = viewModel.userData.currentLesson
    val goalCount     = viewModel.studyGoals.size
    val doneCount     = viewModel.studyGoals.count { it.isCompleted }

    var nameInput       by remember(userName) { mutableStateOf(userName) }
    var nameSaved       by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Avatar
            Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Brush.radialGradient(listOf(Color(0xFF50D8FF), ActiveBlue, Color(0xFF0A90C8)))).border(3.dp, ActiveRing, CircleShape), contentAlignment = Alignment.Center) {
                Text(if (userName.isNotEmpty()) userName.first().uppercaseChar().toString() else "?", color = TextWhite, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text("LinguaQuest Learner 🇰🇷", color = TextMuted, fontSize = 13.sp)

            // Editable name
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.35f))) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("👤 Your Name", color = ActiveBlue, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    HorizontalDivider(color = ActiveBlue.copy(alpha = 0.2f))
                    OutlinedTextField(
                        value = nameInput, onValueChange = { nameInput = it; nameSaved = false },
                        placeholder = { Text("Enter your name...", color = TextMuted, fontSize = 14.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ActiveBlue, unfocusedBorderColor = LockedRing, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite, cursorColor = ActiveBlue, focusedContainerColor = Color(0xFF0F1E2E), unfocusedContainerColor = Color(0xFF0F1E2E)),
                        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { if (nameInput.isNotBlank()) { viewModel.setUserName(nameInput.trim()); nameSaved = true } },
                        enabled = nameInput.isNotBlank() && !nameSaved,
                        modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenColor, disabledContainerColor = GreenColor.copy(alpha = 0.3f))
                    ) { Text(if (nameSaved) "✅ Saved!" else "Save Name", color = TextWhite, fontWeight = FontWeight.ExtraBold) }
                }
            }

            // Stats
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceAround) {
                    ProfileStat("🔥", "2",          "Streak")
                    ProfileStat("💎", "140",         "Gems")
                    ProfileStat("🎯", "$goalCount",  "Goals")
                    ProfileStat("✅", "$doneCount",  "Done")
                }
            }

            // Current lesson
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.35f))) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("📍 Current Lesson", color = ActiveBlue, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    HorizontalDivider(color = ActiveBlue.copy(alpha = 0.2f))
                    if (currentLesson.isNotEmpty()) {
                        Text(currentLesson, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF0F1E2E))) {
                            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3f).clip(RoundedCornerShape(4.dp)).background(Brush.horizontalGradient(listOf(ActiveBlue, GreenColor))))
                        }
                        Text("30% complete", color = TextMuted, fontSize = 11.sp)
                    } else {
                        Text("No lesson started yet.\nGo back home and tap GO! 🚀", color = TextMuted, fontSize = 13.sp, lineHeight = 20.sp)
                    }
                }
            }

            // Achievements
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, GoldColor.copy(alpha = 0.3f))) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🏆 Achievements", color = GoldColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    HorizontalDivider(color = GoldColor.copy(alpha = 0.2f))
                    AchievementRow("🌟", "First Step",    "Complete your first lesson",  unlocked = currentLesson.isNotEmpty())
                    AchievementRow("🎯", "Goal Setter",   "Add your first study goal",   unlocked = goalCount > 0)
                    AchievementRow("✅", "Goal Crusher",  "Complete a study goal",       unlocked = doneCount > 0)
                    AchievementRow("🇰🇷","Korea Bound",  "Complete all 10 units",       unlocked = false)
                }
            }

            // Reset
            OutlinedButton(onClick = { showResetDialog = true }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.6f)), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFEF5350).copy(alpha = 0.08f))) {
                Text("🗑  Reset Progress", color = Color(0xFFEF9A9A), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
        }

        if (showResetDialog) {
            AlertDialog(onDismissRequest = { showResetDialog = false }, containerColor = Color(0xFF142030), shape = RoundedCornerShape(20.dp),
                title = { Text("Reset Progress?", color = TextWhite, fontWeight = FontWeight.ExtraBold) },
                text  = { Text("This will clear your name, lesson, and all goals.", color = TextMuted) },
                confirmButton = { TextButton(onClick = { viewModel.resetProgress(); nameInput = ""; nameSaved = false; showResetDialog = false; onBack() }) { Text("Reset", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold) } },
                dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("Cancel", color = TextMuted) } }
            )
        }
    }
}

@Composable
fun ProfileStat(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(icon, fontSize = 20.sp)
        Text(value, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = TextMuted,  fontSize = 10.sp)
    }
}

@Composable
fun AchievementRow(icon: String, title: String, desc: String, unlocked: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(if (unlocked) GoldColor.copy(alpha = 0.18f) else Color(0xFF0F1E2E)).border(1.dp, if (unlocked) GoldColor.copy(alpha = 0.5f) else LockedRing.copy(alpha = 0.3f), CircleShape), contentAlignment = Alignment.Center) {
            Text(if (unlocked) icon else "🔒", fontSize = 16.sp)
        }
        Column {
            Text(title, color = if (unlocked) TextWhite else TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = TextMuted.copy(alpha = 0.7f), fontSize = 10.sp)
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  SCREEN 6 – LEADERBOARD (bonus)
// ═══════════════════════════════════════════════════════════════
data class LeaderEntry(val rank: Int, val name: String, val xp: Int, val flag: String, val isYou: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(onBack: () -> Unit) {
    val entries = listOf(
        LeaderEntry(1, "지민",   9840, "🇰🇷"),
        LeaderEntry(2, "Hana",  8720, "🇯🇵"),
        LeaderEntry(3, "Aryan", 7650, "🇮🇳"),
        LeaderEntry(4, "Sofia", 6530, "🇧🇷"),
        LeaderEntry(5, "You",    140, "🇲🇾", isYou = true),
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("🏆 This Week's Top Learners", color = GoldColor, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)), border = BorderStroke(1.dp, GoldColor.copy(alpha = 0.3f))) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF0A1628), Color(0xFF0D2235)))).padding(vertical = 24.dp, horizontal = 16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                        PodiumItem(entries[1], 80.dp,  Color(0xFF90A4AE))
                        PodiumItem(entries[0], 110.dp, GoldColor)
                        PodiumItem(entries[2], 60.dp,  Color(0xFFFF8A65))
                    }
                }
            }
            Text("📊 Full Rankings", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            entries.forEach { LeaderboardRow(it) }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.4f))) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFF0A2040), Color(0xFF0D2235)))).padding(20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("⚡ Weekly Challenge", color = ActiveBlue, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Earn 500 XP this week to move up!", color = TextWhite, fontSize = 13.sp)
                        Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF0F1E2E))) {
                            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.28f).clip(RoundedCornerShape(4.dp)).background(Brush.horizontalGradient(listOf(ActiveBlue, GreenColor))))
                        }
                        Text("140 / 500 XP", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun PodiumItem(entry: LeaderEntry, height: androidx.compose.ui.unit.Dp, podiumColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(entry.flag, fontSize = 20.sp)
        Text(entry.name, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text("${entry.xp} XP", color = podiumColor, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
        Box(modifier = Modifier.width(72.dp).height(height).clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).background(podiumColor.copy(alpha = 0.25f)).border(1.dp, podiumColor.copy(alpha = 0.5f), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)), contentAlignment = Alignment.Center) {
            Text(when (entry.rank) { 1 -> "🥇"; 2 -> "🥈"; else -> "🥉" }, fontSize = 24.sp)
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderEntry) {
    val isYou       = entry.isYou
    val rankColor   = when (entry.rank) { 1 -> GoldColor; 2 -> Color(0xFF90A4AE); 3 -> Color(0xFFFF8A65); else -> TextMuted }
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = if (isYou) ActiveBlue.copy(alpha = 0.12f) else Color(0xFF0A1628)), border = BorderStroke(1.dp, if (isYou) ActiveBlue.copy(alpha = 0.5f) else LockedRing.copy(alpha = 0.2f))) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("#${entry.rank}", color = rankColor, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.width(32.dp))
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isYou) ActiveBlue.copy(alpha = 0.2f) else Color(0xFF0F1E2E)).border(1.dp, if (isYou) ActiveBlue else LockedRing.copy(alpha = 0.3f), CircleShape), contentAlignment = Alignment.Center) { Text(entry.flag, fontSize = 18.sp) }
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(entry.name, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                if (isYou) Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(ActiveBlue.copy(alpha = 0.2f)).padding(horizontal = 6.dp, vertical = 2.dp)) { Text("YOU", color = ActiveBlue, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold) }
            }
            Text("${entry.xp} XP", color = GoldColor, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}