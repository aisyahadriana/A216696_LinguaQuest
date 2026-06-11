package com.example.a216696_wan_project2

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216696_wan_project2.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onAddNewGoal: () -> Unit
) {
    val goals     by viewModel.studyGoals.collectAsState()
    val completed  = goals.count { it.isCompleted }
    val total      = goals.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Goals", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1628))
            )
        },
        containerColor = DarkBg,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewGoal, containerColor = GreenColor, contentColor = TextWhite, shape = RoundedCornerShape(16.dp)) {
                Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // SDG header
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)), border = BorderStroke(1.dp, GreenColor.copy(alpha = 0.35f))) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFF0D3B2E), Color(0xFF0A2A20)))).padding(20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("🌍", fontSize = 24.sp)
                            Column {
                                Text("SDG 4 – Quality Education", color = GreenColor, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                                Text("Track your learning goals", color = TextWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                            }
                        }
                        if (total > 0) {
                            HorizontalDivider(color = GreenColor.copy(alpha = 0.2f))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("$completed / $total completed", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("${if (total > 0) (completed * 100 / total) else 0}%", color = GreenColor, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF0F1E2E))) {
                                val progress = if (total > 0) completed.toFloat() / total.toFloat() else 0f
                                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progress).clip(RoundedCornerShape(4.dp)).background(Brush.horizontalGradient(listOf(GreenColor, ActiveBlue))))
                            }
                        }
                    }
                }
            }

            // Empty state
            if (goals.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("🎯", fontSize = 48.sp)
                        Text("No goals yet!", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Tap + to add your first study goal", color = TextMuted, fontSize = 13.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            } else {
                Text("📋 Your Goals (${goals.size})", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                goals.forEach { goal ->
                    RoomGoalCard(
                        goal     = goal,
                        onToggle = { viewModel.toggleGoal(goal.id, goal.isCompleted) },
                        onDelete = { viewModel.deleteGoal(goal.id) }
                    )
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun RoomGoalCard(goal: GoalEntity, onToggle: () -> Unit, onDelete: () -> Unit) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Card(
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = if (goal.isCompleted) Color(0xFF0D3B2E) else Color(0xFF0D2235)),
        border    = BorderStroke(1.dp, if (goal.isCompleted) GreenColor.copy(alpha = 0.4f) else ActiveBlue.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            // Checkbox
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape)
                    .background(if (goal.isCompleted) GreenColor.copy(alpha = 0.2f) else Color(0xFF0F1E2E))
                    .border(2.dp, if (goal.isCompleted) GreenColor else LockedRing.copy(alpha = 0.5f), CircleShape)
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (goal.isCompleted) Text("✓", color = GreenColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    goal.title,
                    color          = if (goal.isCompleted) TextMuted else TextWhite,
                    fontSize       = 14.sp,
                    fontWeight     = FontWeight.Bold,
                    textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(ActiveBlue.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                        Text("📅 ${goal.targetDays} days", color = ActiveBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    if (goal.isCompleted) {
                        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(GreenColor.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                            Text("✅ Done!", color = GreenColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            // Delete button
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape)
                    .background(Color(0xFFEF5350).copy(alpha = 0.1f))
                    .border(1.dp, Color(0xFFEF5350).copy(alpha = 0.3f), CircleShape)
                    .clickable { showDeleteConfirm = true },
                contentAlignment = Alignment.Center
            ) { Text("🗑", fontSize = 14.sp) }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor   = Color(0xFF142030),
            shape            = RoundedCornerShape(20.dp),
            title  = { Text("Delete Goal?", color = TextWhite, fontWeight = FontWeight.ExtraBold) },
            text   = { Text("\"${goal.title}\" will be removed.", color = TextMuted) },
            confirmButton = { TextButton(onClick = { onDelete(); showDeleteConfirm = false }) { Text("Delete", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel", color = TextMuted) } }
        )
    }
}
