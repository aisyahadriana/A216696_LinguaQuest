package com.example.a216696_wan_lab5

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
import com.example.a216696_wan_lab5.ui.theme.*
import kotlin.math.ceil

// ── Progress Calculator Screen ─────────────────────────────────

//   weeksNeeded   = ceil(unitsRemaining × 3 hrs ÷ hoursPerWeek)
//   dailyMinutes  = (hoursPerWeek × 60) ÷ 7
//   xpEarned      = unitsCompleted × 10
//   progressPercent = unitsCompleted ÷ 10 × 100
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressCalculatorScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit
) {
    val userName = viewModel.userData.userName

    // Local input state
    var hoursPerWeek   by remember { mutableStateOf(5f) }
    var unitsCompleted by remember { mutableStateOf(0) }
    var calculated     by remember { mutableStateOf(false) }

    // ── All calculations ───────────────────────────────────────
    val totalUnits       = 10
    val unitsRemaining   = totalUnits - unitsCompleted
    val hoursPerUnit     = 3.0
    val totalHoursNeeded = unitsRemaining * hoursPerUnit
    val weeksNeeded      = if (hoursPerWeek > 0) ceil(totalHoursNeeded / hoursPerWeek).toInt() else 0
    val dailyMinutes     = if (hoursPerWeek > 0) ((hoursPerWeek * 60) / 7).toInt() else 0
    val xpEarned         = unitsCompleted * 10
    val xpRemaining      = unitsRemaining * 10
    val progressPercent  = unitsCompleted * 100 / totalUnits

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progress Calculator", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Header card ─────────────────────────────────────
            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.4f))
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFF0A1628), Color(0xFF0D2235)))).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("📊", fontSize = 28.sp)
                        Column {
                            Text("SDG 4 – Study Planner", color = ActiveBlue, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            Text(
                                if (userName.isNotEmpty()) "Let's plan your journey, $userName!" else "Calculate how long to finish LinguaQuest!",
                                color = TextWhite.copy(alpha = 0.75f), fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // ── Inputs ──────────────────────────────────────────
            Text("⚙️ Your Study Plan", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)

            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border = BorderStroke(1.dp, LockedRing.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {

                    // Slider — hours per week
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("⏱️ Study hours per week", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(ActiveBlue.copy(alpha = 0.2f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                                Text("${hoursPerWeek.toInt()} hrs", color = ActiveBlue, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                        Slider(
                            value = hoursPerWeek,
                            onValueChange = { hoursPerWeek = it; calculated = false },
                            valueRange = 1f..20f,
                            steps = 18,
                            colors = SliderDefaults.colors(thumbColor = ActiveBlue, activeTrackColor = ActiveBlue, inactiveTrackColor = LockedRing.copy(alpha = 0.3f))
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("1 hr", color = TextMuted, fontSize = 10.sp)
                            Text("20 hrs", color = TextMuted, fontSize = 10.sp)
                        }
                    }

                    HorizontalDivider(color = LockedRing.copy(alpha = 0.2f))

                    // Units completed grid
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("✅ Units already completed", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        // Row 1: 0–5
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            (0..5).forEach { n ->
                                val sel = unitsCompleted == n
                                Box(
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                                        .background(if (sel) GreenColor.copy(alpha = 0.2f) else Color(0xFF0A1628))
                                        .border(1.5.dp, if (sel) GreenColor else LockedRing.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                        .clickable { unitsCompleted = n; calculated = false }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) { Text("$n", color = if (sel) GreenColor else TextWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold) }
                            }
                        }
                        // Row 2: 6–10
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            (6..10).forEach { n ->
                                val sel = unitsCompleted == n
                                Box(
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                                        .background(if (sel) GreenColor.copy(alpha = 0.2f) else Color(0xFF0A1628))
                                        .border(1.5.dp, if (sel) GreenColor else LockedRing.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                        .clickable { unitsCompleted = n; calculated = false }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) { Text("$n", color = if (sel) GreenColor else TextWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold) }
                            }
                        }
                    }
                }
            }

            // ── Calculate button ────────────────────────────────
            Button(
                onClick  = { calculated = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = ActiveBlue)
            ) {
                Text("CALCULATE 🧮", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }

            // ── Results (shown after Calculate is tapped) ───────
            if (calculated) {

                Text("📈 Your Results", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)

                // Progress bar
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.35f))) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Overall Progress", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("$progressPercent%", color = GreenColor, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Box(modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)).background(Color(0xFF0F1E2E))) {
                            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progressPercent / 100f).clip(RoundedCornerShape(6.dp)).background(Brush.horizontalGradient(listOf(GreenColor, ActiveBlue))))
                        }
                        Text("$unitsCompleted of $totalUnits units completed", color = TextMuted, fontSize = 11.sp)
                    }
                }

                // Stat cards row 1
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ResultStatCard(Modifier.weight(1f), "📅", if (unitsRemaining == 0) "Done!" else "$weeksNeeded wks", "to finish", if (unitsRemaining == 0) GreenColor else GoldColor)
                    ResultStatCard(Modifier.weight(1f), "⏱️", "${dailyMinutes}m", "per day needed", ActiveBlue)
                }

                // Stat cards row 2
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ResultStatCard(Modifier.weight(1f), "⭐", "$xpEarned XP", "earned so far", GoldColor)
                    ResultStatCard(Modifier.weight(1f), "🎯", "$xpRemaining XP", "left to earn", Color(0xFFCE93D8))
                }

                // Motivational card with formula shown
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, GoldColor.copy(alpha = 0.3f))) {
                    Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFF1A1200), Color(0xFF0D2235)))).padding(20.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(when { unitsRemaining == 0 -> "🎉"; weeksNeeded <= 4 -> "🚀"; weeksNeeded <= 10 -> "💪"; else -> "🌱" }, fontSize = 32.sp)
                            Text(
                                when {
                                    unitsRemaining == 0 -> "Congratulations! You've completed LinguaQuest!"
                                    weeksNeeded <= 4    -> "You're on fire! Only $weeksNeeded week${if (weeksNeeded == 1) "" else "s"} to go!"
                                    weeksNeeded <= 10   -> "Great pace! $weeksNeeded weeks with ${hoursPerWeek.toInt()} hrs/week — you've got this!"
                                    else                -> "Study a little more each week to finish faster!"
                                },
                                color = GoldColor, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center, lineHeight = 20.sp
                            )
                            HorizontalDivider(color = GoldColor.copy(alpha = 0.2f))
                            // Shows the calculation formula — great for Q&A explanation!
                            Text("📐 How we calculated:", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "$unitsRemaining units × ${hoursPerUnit.toInt()} hrs = ${totalHoursNeeded.toInt()} hrs needed\n" +
                                        "${totalHoursNeeded.toInt()} hrs ÷ ${hoursPerWeek.toInt()} hrs/week = $weeksNeeded weeks",
                                color = TextMuted.copy(alpha = 0.8f), fontSize = 11.sp,
                                textAlign = TextAlign.Center, lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Result Stat Card helper ────────────────────────────────────
@Composable
fun ResultStatCard(modifier: Modifier, icon: String, value: String, label: String, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)), border = BorderStroke(1.dp, color.copy(alpha = 0.3f))) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(icon,  fontSize = 22.sp)
            Text(value, color = color,     fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Text(label, color = TextMuted, fontSize = 10.sp, textAlign = TextAlign.Center, lineHeight = 14.sp)
        }
    }
}
