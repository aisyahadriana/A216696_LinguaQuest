package com.example.a216696_wan_lab4

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216696_wan_lab4.ui.theme.*

// ── Lesson Detail Screen ───────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit
) {
    val userName      = viewModel.userData.userName
    val currentLesson = viewModel.userData.currentLesson

    var selectedAnswer  by remember { mutableStateOf("") }
    var answerSubmitted by remember { mutableStateOf(false) }

    val correctAnswer = "공항"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "Lesson",
                        color      = TextWhite,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint               = TextWhite
                        )
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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Greeting banner ─────────────────────────────────
            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.4f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF0A1628), Color(0xFF0D2235)))
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("✈️  안녕하세요, $userName!", color = GreenColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text(
                            text       = "📍 Now studying: $currentLesson",
                            color      = TextWhite.copy(alpha = 0.75f),
                            fontSize   = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldColor.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("⭐ +10 XP this lesson", color = GoldColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // ── Start Lesson Button ───────────────────────────────
            val startInteraction = remember { MutableInteractionSource() }
            val startPressed by startInteraction.collectIsPressedAsState()

            Button(
                onClick       = { /* nothing yet*/ },
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .scale(if (startPressed) 0.97f else 1f),
                shape         = RoundedCornerShape(16.dp),
                interactionSource = startInteraction,
                colors        = ButtonDefaults.buttonColors(
                    containerColor = if (startPressed) GreenColor.copy(alpha = 0.75f) else GreenColor
                ),
                elevation     = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 1.dp
                )
            ) {
                Text(
                    text       = "🚀  Start Lesson",
                    color      = TextWhite,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // ── Vocabulary Section ──────────────────────────────
            SectionTitle("📚 Vocabulary")

            val vocab = listOf(
                Triple("✈️", "공항",   "Airport"),
                Triple("🎫", "표",     "Ticket"),
                Triple("🗣️","어디에요?","Where is it?"),
                Triple("🧳", "짐",     "Luggage"),
                Triple("🚪", "출구",   "Exit / Gate")
            )

            vocab.forEach { (icon, korean, english) ->
                VocabRow(icon = icon, korean = korean, english = english)
            }

            // ── Mini Quiz ───────────────────────────────────────
            SectionTitle("❓ Quick Quiz")

            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text       = "What is the Korean word for \"Airport\"?",
                        color      = TextWhite,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    val options = listOf("비행기", "공항", "출구", "표")
                    options.forEach { option ->
                        val isSelected = selectedAnswer == option
                        val isCorrect  = answerSubmitted && option == correctAnswer
                        val isWrong    = answerSubmitted && isSelected && option != correctAnswer

                        val borderColor = when {
                            isCorrect  -> GreenColor
                            isWrong    -> Color(0xFFEF5350)
                            isSelected -> ActiveBlue
                            else       -> LockedRing.copy(alpha = 0.4f)
                        }
                        val bgColor = when {
                            isCorrect  -> GreenColor.copy(alpha = 0.12f)
                            isWrong    -> Color(0xFFEF5350).copy(alpha = 0.1f)
                            isSelected -> ActiveBlue.copy(alpha = 0.12f)
                            else       -> Color(0xFF0F1E2E)
                        }

                        OutlinedButton(
                            onClick  = { if (!answerSubmitted) selectedAnswer = option },
                            modifier = Modifier.fillMaxWidth(),
                            shape    = RoundedCornerShape(12.dp),
                            border   = BorderStroke(1.5.dp, borderColor),
                            colors   = ButtonDefaults.outlinedButtonColors(containerColor = bgColor)
                        ) {
                            Text(
                                text       = option,
                                color      = TextWhite,
                                fontSize   = 14.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
                            )
                        }
                    }

                    if (!answerSubmitted) {
                        Button(
                            onClick  = { if (selectedAnswer.isNotEmpty()) answerSubmitted = true },
                            enabled  = selectedAnswer.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = GreenColor)
                        ) {
                            Text("CHECK ANSWER ✅", color = TextWhite, fontWeight = FontWeight.ExtraBold)
                        }
                    } else {
                        val isRight = selectedAnswer == correctAnswer
                        Card(
                            shape  = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isRight) GreenColor.copy(alpha = 0.15f)
                                else Color(0xFFEF5350).copy(alpha = 0.12f)
                            )
                        ) {
                            Text(
                                text       = if (isRight) "🎉 Correct! 공항 means Airport." else "❌ Not quite. The answer is 공항 (Airport).",
                                color      = if (isRight) GreenColor else Color(0xFFEF9A9A),
                                modifier   = Modifier.padding(16.dp),
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Helper Composables ─────────────────────────────────────────

@Composable
fun SectionTitle(text: String) {
    Text(
        text       = text,
        color      = TextWhite,
        fontSize   = 16.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun VocabRow(icon: String, korean: String, english: String) {
    Card(
        shape  = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)),
        border = BorderStroke(1.dp, LockedRing.copy(alpha = 0.3f))
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier         = Modifier.size(40.dp).clip(CircleShape)
                    .background(ActiveBlue.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) { Text(icon, fontSize = 18.sp) }

            Column {
                Text(korean,  color = TextWhite,                   fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                Text(english, color = TextWhite.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
    }
}