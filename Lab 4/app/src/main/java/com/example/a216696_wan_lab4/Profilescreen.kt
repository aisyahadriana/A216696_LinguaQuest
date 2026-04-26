package com.example.a216696_wan_lab4

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import com.example.a216696_wan_lab4.ui.theme.*
import androidx.compose.foundation.BorderStroke

// ── Profile Screen ─────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit
) {
    val storedName    = viewModel.userData.userName.ifEmpty { "Eleanor" }
    val currentLesson = viewModel.userData.currentLesson

    LaunchedEffect(Unit) {
        if (viewModel.userData.userName.isEmpty()) {
            viewModel.setUserName("Eleanor")
        }
    }

    var showResetDialog   by remember { mutableStateOf(false) }
    var showEditDialog    by remember { mutableStateOf(false) }
    var editNameInput     by remember { mutableStateOf(storedName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        editNameInput = storedName
                        showEditDialog = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit name", tint = ActiveBlue)
                    }
                },
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
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Avatar ──────────────────────────────────────────
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(listOf(Color(0xFF50D8FF), ActiveBlue, Color(0xFF0A90C8)))
                        )
                        .border(3.dp, ActiveRing, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = storedName.first().uppercaseChar().toString(),
                        color      = TextWhite,
                        fontSize   = 42.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Small edit badge on avatar
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ActiveBlue)
                        .border(2.dp, DarkBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint               = TextWhite,
                        modifier           = Modifier.size(14.dp)
                    )
                }
            }

            // ── Name display ─────────────────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text       = storedName,
                    color      = TextWhite,
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign  = TextAlign.Center
                )
                Text(
                    text  = "LinguaQuest Learner 🇰🇷",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                OutlinedButton(
                    onClick = {
                        editNameInput = storedName
                        showEditDialog = true
                    },
                    shape  = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.4f)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = ActiveBlue, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Edit Name", color = ActiveBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // ── Stats row ────────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStat("🔥", "2",   "Streak")
                    ProfileStat("💎", "140", "Gems")
                    ProfileStat("⚡", "25",  "XP")
                }
            }

            // ── Current lesson progress ──────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(20.dp),
                colors   = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border   = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("📍 Current Lesson Progress", color = ActiveBlue, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    HorizontalDivider(color = ActiveBlue.copy(alpha = 0.2f))
                    if (currentLesson.isNotEmpty()) {
                        Text(currentLesson, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier.fillMaxWidth().height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF0F1E2E))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.horizontalGradient(listOf(ActiveBlue, GreenColor))
                                    )
                            )
                        }
                        Text("30% complete", color = TextMuted, fontSize = 11.sp)
                    } else {
                        Text(
                            text     = "No lesson started yet.\nGo back home and tap GO! 🚀",
                            color    = TextMuted,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // ── Achievements section ─────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(20.dp),
                colors   = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border   = BorderStroke(1.dp, GoldColor.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🏆 Achievements", color = GoldColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    HorizontalDivider(color = GoldColor.copy(alpha = 0.2f))
                    AchievementRow("🌟", "First Step",    "Complete your first lesson", unlocked = currentLesson.isNotEmpty())
                    AchievementRow("🔥", "On Fire",       "Maintain a 3-day streak",    unlocked = false)
                    AchievementRow("💯", "Perfect Score", "Get 100% on a quiz",         unlocked = false)
                    AchievementRow("🇰🇷","Korea Bound",  "Complete all 10 units",      unlocked = false)
                }
            }

            // ── Reset button ─────────────────────────────────────
            OutlinedButton(
                onClick  = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(14.dp),
                border   = BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.6f)),
                colors   = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFEF5350).copy(alpha = 0.08f))
            ) {
                Text("🗑  Reset Progress", color = Color(0xFFEF9A9A), fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
        }

        // ── Edit Name Dialog ─────────────────────────────────────
        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                containerColor   = Color(0xFF142030),
                shape            = RoundedCornerShape(20.dp),
                title            = { Text("Edit Your Name ✏️", color = TextWhite, fontWeight = FontWeight.ExtraBold) },
                text             = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("What should we call you?", color = TextMuted, fontSize = 13.sp)
                        OutlinedTextField(
                            value         = editNameInput,
                            onValueChange = { editNameInput = it },
                            placeholder   = { Text("Your name...", color = TextMuted, fontSize = 14.sp) },
                            singleLine    = true,
                            colors        = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor      = ActiveBlue,
                                unfocusedBorderColor    = LockedRing,
                                focusedTextColor        = TextWhite,
                                unfocusedTextColor      = TextWhite,
                                cursorColor             = ActiveBlue,
                                focusedContainerColor   = Color(0xFF0F1E2E),
                                unfocusedContainerColor = Color(0xFF0F1E2E)
                            ),
                            shape    = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val trimmed = editNameInput.trim()
                            if (trimmed.isNotEmpty()) {
                                viewModel.setUserName(trimmed)
                            }
                            showEditDialog = false
                        }
                    ) {
                        Text("Save", color = GreenColor, fontWeight = FontWeight.ExtraBold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }

        // ── Reset confirm dialog ─────────────────────────────────
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                containerColor   = Color(0xFF142030),
                shape            = RoundedCornerShape(20.dp),
                title            = { Text("Reset Progress?", color = TextWhite, fontWeight = FontWeight.ExtraBold) },
                text             = { Text("This will clear your name and lesson progress.", color = TextMuted) },
                confirmButton    = {
                    TextButton(onClick = {
                        viewModel.resetProgress()
                        viewModel.setUserName("Eleanor")
                        showResetDialog = false
                        onBack()
                    }) {
                        Text("Reset", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton    = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }
    }
}

// ── Helper Composables ─────────────────────────────────────────

@Composable
fun ProfileStat(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(icon,  fontSize = 22.sp)
        Text(value, color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = TextMuted,  fontSize = 10.sp)
    }
}

@Composable
fun AchievementRow(icon: String, title: String, desc: String, unlocked: Boolean) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(CircleShape)
                .background(if (unlocked) GoldColor.copy(alpha = 0.18f) else Color(0xFF0F1E2E))
                .border(1.dp, if (unlocked) GoldColor.copy(alpha = 0.5f) else LockedRing.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(if (unlocked) icon else "🔒", fontSize = 16.sp)
        }
        Column {
            Text(title, color = if (unlocked) TextWhite else TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(desc,  color = TextMuted.copy(alpha = 0.7f), fontSize = 10.sp)
        }
    }
}