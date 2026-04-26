package com.example.a216696_wan_lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216696_wan_lab1.ui.theme.A216696_Wan_Lab1Theme

// ── Colour Palette ──────────────────────────────────────
val DarkBg       = Color(0xFF0F1923)
val NavBg        = Color(0xFF0A1018)
val BannerBlue   = Color(0xFF1899D6)
val BannerDark   = Color(0xFF1070A0)
val ActiveBlue   = Color(0xFF1CB0F6)
val ActiveRing   = Color(0xFF6BDBFF)
val ActiveShadow = Color(0xFF0A6090)
val LockedFill   = Color(0xFF1A2535)
val LockedRing   = Color(0xFF2A3D52)
val LockedShadow = Color(0xFF080F18)
val TextWhite    = Color(0xFFFFFFFF)
val TextMuted    = Color(0xFF3D6080)
val GoldColor    = Color(0xFFFFB300)
val GreenColor   = Color(0xFF58CC02)

// ── Activity ───────────────────────────────────────────────────
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216696_Wan_Lab1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
// ── Main Screen ────────────────────────────────────────────────
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            AppHeader()
            TopStatsBar()
            SectionBanner()
            Spacer(Modifier.height(28.dp))
            LessonPath()
            Spacer(Modifier.height(32.dp))
        }
        BottomNavBar()
    }
}
// ── App Header ─────────────────────────────────────────────────
@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1628), DarkBg)))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ActiveBlue.copy(alpha = 0.07f))
        )
        Text(
            text = "LinguaQuest",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite,
            letterSpacing = 3.sp
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(100.dp)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, ActiveBlue, Color.Transparent)
                    )
                )
        )
    }
}
// ── Top Stats Bar ──────────────────────────────────────────────
@Composable
fun TopStatsBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBg)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🇰🇷", fontSize = 22.sp)
            Spacer(Modifier.width(5.dp))
            Text("10", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🔥", fontSize = 20.sp)
            Spacer(Modifier.width(4.dp))
            Text("2", color = Color(0xFFFF9800), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("💎", fontSize = 20.sp)
            Spacer(Modifier.width(4.dp))
            Text("140", color = Color(0xFF4DD0E1), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⚡", fontSize = 20.sp)
            Spacer(Modifier.width(4.dp))
            Text("25", color = Color(0xFFCE93D8), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}
// ── Section Banner ─────────────────────────────────────────────
@Composable
fun SectionBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(BannerBlue, BannerDark)))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "SECTION 2, UNIT 1",
                    color = Color(0xFFBBDEFB),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Find your way at the airport",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(170.dp)
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x44000000))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(55.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFBBDEFB))
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x44000000))
                    .border(1.dp, Color(0x6664B5F6), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📋", fontSize = 22.sp)
            }
        }
    }
}
// ── Lesson Data ────────────────────────────────────────────────
data class LessonNode(
    val icon: String,
    val unit: String,
    val topic: String,
    val isActive: Boolean
)
data class StarData(
    val x: Dp,
    val y: Dp,
    val size: androidx.compose.ui.unit.TextUnit,
    val alpha: Float,
    val glyph: String
)
// ── Lesson Path ────────────────────────────────────────────────
@Composable
fun LessonPath() {
    val lessons = listOf(
        LessonNode("⭐",  "Unit 1",  "Find Your Way\nat the Airport", isActive = true),
        LessonNode("✈️", "Unit 2",  "On the Plane",                  isActive = false),
        LessonNode("🗣️","Unit 3",  "Asking for\nDirections",         isActive = false),
        LessonNode("📋", "Unit 4",  "Filling Out\nForms",            isActive = false),
        LessonNode("🛍️","Unit 5",  "Shopping &\nNumbers",           isActive = false),
        LessonNode("🍜", "Unit 6",  "Ordering Food",                 isActive = false),
        LessonNode("🏨", "Unit 7",  "Checking into\na Hotel",        isActive = false),
        LessonNode("📞", "Unit 8",  "Phone\nConversations",          isActive = false),
        LessonNode("🚗", "Unit 9",  "Transport &\nGetting Around",   isActive = false),
        LessonNode("🎓", "Unit 10", "Unit Review",                   isActive = false),
    )

    val stars = listOf(
        StarData(10.dp,   20.dp, 7.sp,  0.30f, "✦"),
        StarData(355.dp,  15.dp, 5.sp,  0.22f, "✧"),
        StarData(342.dp,  95.dp, 8.sp,  0.25f, "✦"),
        StarData(12.dp,  158.dp, 5.sp,  0.20f, "✧"),
        StarData(360.dp, 202.dp, 6.sp,  0.28f, "✦"),
        StarData(20.dp,  278.dp, 8.sp,  0.22f, "✧"),
        StarData(346.dp, 308.dp, 5.sp,  0.25f, "✦"),
        StarData(10.dp,  388.dp, 6.sp,  0.20f, "✧"),
        StarData(358.dp, 400.dp, 7.sp,  0.28f, "✦"),
        StarData(16.dp,  478.dp, 5.sp,  0.22f, "✧"),
        StarData(350.dp, 500.dp, 8.sp,  0.25f, "✦"),
        StarData(8.dp,   570.dp, 6.sp,  0.20f, "✧"),
        StarData(362.dp, 590.dp, 5.sp,  0.28f, "✦"),
        StarData(18.dp,  660.dp, 7.sp,  0.22f, "✧"),
        StarData(344.dp, 678.dp, 6.sp,  0.25f, "✦"),
        StarData(12.dp,  750.dp, 5.sp,  0.20f, "✧"),
        StarData(356.dp, 762.dp, 8.sp,  0.28f, "✦"),
        StarData(26.dp,  840.dp, 6.sp,  0.22f, "✧"),
        StarData(340.dp, 855.dp, 5.sp,  0.25f, "✦"),
        StarData(14.dp,  930.dp, 7.sp,  0.20f, "✧"),
    )

    Box(modifier = Modifier.fillMaxWidth()) {

        // Layer 1 — stars
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1050.dp)
        ) {
            stars.forEach { star ->
                Box(modifier = Modifier.offset(x = star.x, y = star.y)) {
                    Text(star.glyph, fontSize = star.size, color = Color.White.copy(alpha = star.alpha))
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            lessons.forEachIndexed { index, lesson ->
                val bubbleOnLeft = index % 2 == 0
                ZigzagRow(lesson = lesson, bubbleOnLeft = bubbleOnLeft)

                if (index < lessons.size - 1) {
                    val nextBubbleOnLeft = (index + 1) % 2 == 0
                    // Arrows point TOWARD the next bubble's side
                    DiagonalArrow(nextBubbleOnLeft = nextBubbleOnLeft, isActivePath = lesson.isActive)
                }
            }
        }
    }
}
// ── Zigzag Row ─────────────────────────────────────────────────
@Composable
fun ZigzagRow(lesson: LessonNode, bubbleOnLeft: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (bubbleOnLeft) Arrangement.Start else Arrangement.End
    ) {
        if (bubbleOnLeft) {
            Spacer(Modifier.width(24.dp))
            BubbleCircle(lesson = lesson)
            Spacer(Modifier.width(14.dp))
            LessonLabel(lesson = lesson, alignEnd = false)
        } else {
            LessonLabel(lesson = lesson, alignEnd = true)
            Spacer(Modifier.width(14.dp))
            BubbleCircle(lesson = lesson)
            Spacer(Modifier.width(24.dp))
        }
    }
}
// ── Bubble Circle ──────────────────────────────────────────────
@Composable
fun BubbleCircle(lesson: LessonNode) {
    Box(
        modifier = Modifier.size(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .offset(y = 6.dp)
                .clip(CircleShape)
                .background(if (lesson.isActive) ActiveShadow else LockedShadow)
        )
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (lesson.isActive)
                        Brush.radialGradient(listOf(Color(0xFF50D8FF), ActiveBlue, Color(0xFF0A90C8)))
                    else
                        Brush.radialGradient(listOf(Color(0xFF1E3045), LockedFill))
                )
                .border(
                    width = if (lesson.isActive) 3.dp else 2.dp,
                    color  = if (lesson.isActive) ActiveRing else LockedRing,
                    shape  = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = (-11).dp, y = (-11).dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = if (lesson.isActive) 0.28f else 0.07f))
            )
            Text(lesson.icon, fontSize = 24.sp, textAlign = TextAlign.Center)
        }
        if (!lesson.isActive) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .clip(CircleShape)
                    .background(DarkBg)
                    .border(1.dp, LockedRing, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🔒", fontSize = 8.sp, textAlign = TextAlign.Center)
            }
        }
        if (lesson.isActive) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GreenColor)
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text("GO", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}
// ── Lesson Label ───────────────────────────────────────────────
@Composable
fun LessonLabel(lesson: LessonNode, alignEnd: Boolean) {
    Column(
        modifier = Modifier.width(110.dp),
        horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (lesson.isActive) ActiveBlue.copy(alpha = 0.18f)
                    else LockedRing.copy(alpha = 0.12f)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = lesson.unit,
                color = if (lesson.isActive) ActiveBlue else TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = lesson.topic,
            color = if (lesson.isActive) TextWhite else TextMuted,
            fontSize = 12.sp,
            fontWeight = if (lesson.isActive) FontWeight.ExtraBold else FontWeight.SemiBold,
            textAlign = if (alignEnd) TextAlign.End else TextAlign.Start,
            lineHeight = 16.sp
        )
        if (lesson.isActive) {
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(GoldColor.copy(alpha = 0.15f))
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text("⭐ +10 XP", color = GoldColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun DiagonalArrow(nextBubbleOnLeft: Boolean, isActivePath: Boolean) {

    val angle = if (nextBubbleOnLeft) 135f else 45f

    val arrowColor = Color.White.copy(alpha = if (isActivePath) 0.55f else 0.22f)
    val arrowSize  = 30.sp

    val startX = if (nextBubbleOnLeft) 260.dp else  60.dp
    val endX   = if (nextBubbleOnLeft)  60.dp else 260.dp

    val totalHeight = 90.dp
    val steps = 5

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        repeat(steps) { i ->
            val fraction = i.toFloat() / (steps - 1).toFloat()

            val xDiff = endX - startX
            val arrowX = startX + (xDiff * fraction)
            val arrowY = (totalHeight - 30.dp) * fraction

            Box(
                modifier = Modifier.offset(x = arrowX, y = arrowY)
            ) {
                Text(
                    text = "\uD83D\uDC3E",
                    fontSize = arrowSize,
                    fontWeight = FontWeight.Black,
                    color = arrowColor,
                    modifier = Modifier.rotate(angle)
                )
            }
        }
    }
}
// ── Bottom Nav Bar ─────────────────────────────────────────────
@Composable
fun BottomNavBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavBg)
            .padding(vertical = 10.dp, horizontal = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(icon = "🏠", label = "Home",        isActive = true)
            NavItem(icon = "🏆", label = "Leaderboard", isActive = false)
            NavItem(icon = "💚", label = "Hearts",      isActive = false)
            NavItem(icon = "💬", label = "Chat",        isActive = false)
            NavItem(icon = "👤", label = "Profile",     isActive = false)
        }
    }
}
@Composable
fun NavItem(icon: String, label: String, isActive: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isActive) Color(0xFF1A3050) else Color.Transparent)
            .border(
                width = if (isActive) 1.dp else 0.dp,
                color = if (isActive) ActiveBlue.copy(0.35f) else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(icon, fontSize = 24.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            label,
            color = if (isActive) ActiveBlue else TextMuted,
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Normal
        )
    }
}
// ── Preview ────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0F1923)
@Composable
fun MainScreenPreview() {
    A216696_Wan_Lab1Theme {
        MainScreen()
    }
}