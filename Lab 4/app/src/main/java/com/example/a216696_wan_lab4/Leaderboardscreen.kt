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
import androidx.compose.foundation.BorderStroke
import com.example.a216696_wan_lab4.ui.theme.*

// ── Data ───────────────────────────────────────────────────────
data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val flag: String,
    val xp: Int,
    val streak: Int,
    val isCurrentUser: Boolean = false
)

// ── Leaderboard Screen ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit
) {
    val userName = viewModel.userData.userName.ifEmpty { "Eleanor" }

    // Mock leaderboard data — current user is injected at rank 4
    val entries = listOf(
        LeaderboardEntry(1, "Syasya",   "🇲🇾", 482, 21),
        LeaderboardEntry(2, "Nadiah",   "🇪🇸", 396, 15),
        LeaderboardEntry(3, "Mark",    "🇰🇷", 321,  9),
        LeaderboardEntry(4, userName,     "🇲🇾",  140,  2, isCurrentUser = true),
        LeaderboardEntry(5, "Priya",   "🇮🇳",   95,  1),
        LeaderboardEntry(6, "Carlos",  "🇪🇸",   70,  1),
        LeaderboardEntry(7, "Amelia",  "🇬🇧",   50,  0),
        LeaderboardEntry(8, "Diego",   "🇧🇷",   35,  0),
        LeaderboardEntry(9, "Fatima",  "🇸🇦",   20,  0),
        LeaderboardEntry(10,"Andy",    "🇩🇪",   10,  0),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Leaderboard", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                },
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
        ) {

            // ── Header banner ────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF0A1628), Color(0xFF0D1F38)))
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🏆", fontSize = 40.sp)
                    Text(
                        text       = "Weekly Rankings",
                        color      = GoldColor,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text      = "Top Korean learners this week",
                        color     = TextMuted,
                        fontSize  = 12.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    // Reset timer chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF0D2235))
                            .border(1.dp, GoldColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("⏱  Resets in 4d 12h", color = GoldColor.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // ── Top 3 podium ─────────────────────────────────────
            val top3 = entries.take(3)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment     = Alignment.Bottom
                ) {
                    // 2nd place
                    PodiumItem(entry = top3[1], podiumHeight = 64.dp)
                    // 1st place
                    PodiumItem(entry = top3[0], podiumHeight = 90.dp)
                    // 3rd place
                    PodiumItem(entry = top3[2], podiumHeight = 44.dp)
                }
            }

            HorizontalDivider(
                modifier  = Modifier.padding(horizontal = 20.dp),
                color     = Color(0xFF1A2E44),
                thickness = 1.dp
            )

            Spacer(Modifier.height(8.dp))

            // ── Full list ────────────────────────────────────────
            entries.forEach { entry ->
                LeaderboardRow(entry = entry)
            }

            Spacer(Modifier.height(24.dp))

            // ── Motivational footer ──────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
                border   = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("🚀", fontSize = 28.sp)
                    Column {
                        Text("Keep going, $userName!", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Complete more lessons to climb the ranks!", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Podium Item ────────────────────────────────────────────────
@Composable
fun PodiumItem(entry: LeaderboardEntry, podiumHeight: androidx.compose.ui.unit.Dp) {
    val medalColor = when (entry.rank) {
        1 -> GoldColor
        2 -> Color(0xFFB0BEC5)
        3 -> Color(0xFFBF8A60)
        else -> TextMuted
    }
    val medalEmoji = when (entry.rank) {
        1 -> "🥇"; 2 -> "🥈"; else -> "🥉"
    }
    val avatarSize = if (entry.rank == 1) 64.dp else 52.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(medalColor.copy(alpha = 0.3f), Color(0xFF0A1628))
                    )
                )
                .border(2.dp, medalColor.copy(alpha = 0.7f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text     = entry.name.first().uppercaseChar().toString(),
                color    = TextWhite,
                fontSize = if (entry.rank == 1) 26.sp else 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Text(medalEmoji, fontSize = 18.sp)

        Text(
            text       = entry.name.split(" ").first(),
            color      = TextWhite,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center,
            maxLines   = 1
        )

        Text(
            text      = "${entry.xp} XP",
            color     = medalColor,
            fontSize  = 11.sp,
            fontWeight = FontWeight.ExtraBold
        )

        // Podium block
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(podiumHeight)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(medalColor.copy(alpha = 0.25f), medalColor.copy(alpha = 0.08f))
                    )
                )
                .border(
                    1.dp,
                    medalColor.copy(alpha = 0.4f),
                    RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = "#${entry.rank}",
                color      = medalColor,
                fontSize   = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

// ── Leaderboard Row ────────────────────────────────────────────
@Composable
fun LeaderboardRow(entry: LeaderboardEntry) {
    val isCurrentUser = entry.isCurrentUser

    val bgColor = if (isCurrentUser)
        Brush.horizontalGradient(listOf(Color(0xFF0D3B2E), Color(0xFF0D2235)))
    else
        Brush.horizontalGradient(listOf(Color(0xFF0A1628), Color(0xFF0A1628)))

    val borderColor = if (isCurrentUser) GreenColor.copy(alpha = 0.5f) else Color.Transparent

    val rankColor = when (entry.rank) {
        1    -> GoldColor
        2    -> Color(0xFFB0BEC5)
        3    -> Color(0xFFBF8A60)
        else -> TextMuted
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Rank
            Text(
                text       = "#${entry.rank}",
                color      = rankColor,
                fontSize   = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier   = Modifier.width(28.dp),
                textAlign  = TextAlign.Center
            )

            // Avatar circle
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCurrentUser) GreenColor.copy(alpha = 0.18f)
                        else Color(0xFF0F1E2E)
                    )
                    .border(
                        1.dp,
                        if (isCurrentUser) GreenColor.copy(alpha = 0.6f) else LockedRing.copy(alpha = 0.3f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text     = entry.name.first().uppercaseChar().toString(),
                    color    = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Name + flag
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text       = entry.name,
                        color      = if (isCurrentUser) GreenColor else TextWhite,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (isCurrentUser) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GreenColor.copy(alpha = 0.18f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text("YOU", color = GreenColor, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(entry.flag, fontSize = 10.sp)
                    Text("🔥 ${entry.streak}d streak", color = TextMuted, fontSize = 10.sp)
                }
            }

            // XP
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = "${entry.xp}",
                    color      = GoldColor,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text("XP", color = TextMuted, fontSize = 9.sp)
            }
        }
    }
}