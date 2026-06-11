package com.example.a216696_wan_project2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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

// ── Add Goal Screen ────────────────────────────────────────────
// On submit, calls viewModel.addGoal() which adds to the shared list.
// After adding, onGoalAdded() navigates to GoalsScreen automatically.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onGoalAdded: () -> Unit
) {
    // Local form state
    var goalTitle  by remember { mutableStateOf("") }
    var targetDays by remember { mutableStateOf(7) }
    var showError  by remember { mutableStateOf(false) }

    val templates = listOf(
        "Learn 10 new words",
        "Complete 1 lesson",
        "Practice speaking 5 mins",
        "Review flashcards",
        "Watch a short lesson clip"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Study Goal", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
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
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── SDG badge ───────────────────────────────────────
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFF0D3B2E), Color(0xFF0A2A20))))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("🌍", fontSize = 28.sp)
                    Column {
                        Text("SDG 4 – Quality Education", color = GreenColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Setting goals promotes lifelong learning!", color = TextWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                    }
                }
            }

            // ── Goal title input ────────────────────────────────
            Text("📝 What's your goal?", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)

            OutlinedTextField(
                value         = goalTitle,
                onValueChange = { goalTitle = it; showError = false },
                placeholder   = { Text("e.g. Learn 10 ${viewModel.appLessonLanguage.displayName} words today", color = TextMuted, fontSize = 13.sp) },
                singleLine    = true,
                isError       = showError,
                supportingText = if (showError) {{ Text("Please enter a goal!", color = Color(0xFFEF5350)) }} else null,
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = GreenColor,
                    unfocusedBorderColor    = LockedRing,
                    focusedTextColor        = TextWhite,
                    unfocusedTextColor      = TextWhite,
                    cursorColor             = GreenColor,
                    focusedContainerColor   = Color(0xFF0F1E2E),
                    unfocusedContainerColor = Color(0xFF0F1E2E)
                ),
                shape    = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ── Quick templates ─────────────────────────────────
            Text("⚡ Quick templates", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            templates.forEach { template ->
                val isSelected = goalTitle == template
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) GreenColor.copy(alpha = 0.15f) else Color(0xFF0A1628))
                        .border(1.dp, if (isSelected) GreenColor.copy(alpha = 0.6f) else LockedRing.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { goalTitle = template; showError = false }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(template, color = if (isSelected) GreenColor else TextWhite.copy(alpha = 0.8f), fontSize = 13.sp)
                        if (isSelected) Text("✓", color = GreenColor, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            // ── Target days picker ──────────────────────────────
            Text("📅 Complete within how many days?", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)

            val dayOptions = listOf(1, 3, 7, 14, 30)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dayOptions.forEach { days ->
                    val isSelected = targetDays == days
                    Box(
                        modifier = Modifier.weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) ActiveBlue.copy(alpha = 0.2f) else Color(0xFF0A1628))
                            .border(1.5.dp, if (isSelected) ActiveBlue else LockedRing.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable { targetDays = days }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$days", color = if (isSelected) ActiveBlue else TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Text(if (days == 1) "day" else "days", color = TextMuted, fontSize = 9.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // ── Submit button ───────────────────────────────────
            Button(
                onClick = {
                    if (goalTitle.isBlank()) {
                        showError = true
                    } else {
                        viewModel.addGoal(title = goalTitle.trim(), targetDays = targetDays)
                        onGoalAdded()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = GreenColor)
            ) {
                Text("ADD GOAL 🎯", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
