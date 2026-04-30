package com.example.a216696_wan_project1

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216696_wan_project1.ui.theme.*

data class LessonNode(val icon: String, val unit: String, val topic: String, val isActive: Boolean)

@Composable
fun MainScreen(
    viewModel: UserViewModel,
    onGoToLesson: (String) -> Unit,
    onGoToProfile: () -> Unit,
    onGoToLeaderboard: () -> Unit,
    onGoToGoals: () -> Unit,
    modifier: Modifier = Modifier
) {
    val welcomeName = viewModel.userData.userName
    val goalCount   = viewModel.studyGoals.size
    var activeNav   by remember { mutableStateOf("Home") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(DarkBg)) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                AppHeader()
                StatsCard()
                if (welcomeName.isNotEmpty()) WelcomeBanner(name = welcomeName)

                // ── SDG Goals Quick Card ─────────────────────────
                // Shows how many goals the user has set.
                // Tapping it navigates to the Goals screen.
                SdgGoalsBanner(goalCount = goalCount, onTap = onGoToGoals)

                SectionBannerCard()
                Spacer(Modifier.height(28.dp))
                LessonPath(onGoClick = { lessonTitle -> onGoToLesson(lessonTitle) })
                Spacer(Modifier.height(32.dp))
            }
            BottomNavBar(
                activeNav   = activeNav,
                onNavSelect = { selected ->
                    activeNav = selected
                    when (selected) {
                        "Profile"     -> onGoToProfile()
                        "Leaderboard" -> onGoToLeaderboard()
                        "Goals"       -> onGoToGoals()
                    }
                }
            )
        }
    }
}

// ── SDG Goals Banner ───────────────────────────────────────────
// Links home screen to the Goals feature.
// goalCount comes from ViewModel — updates automatically when goals are added.
@Composable
fun SdgGoalsBanner(goalCount: Int, onTap: () -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onTap() },
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
        border    = BorderStroke(1.dp, GreenColor.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFF0D3B2E), Color(0xFF0A2A20))))
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🎯", fontSize = 28.sp)
                Column {
                    Text("SDG 4 – Study Goals", color = GreenColor, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    Text(
                        if (goalCount == 0) "No goals yet — tap to add one!"
                        else "$goalCount goal${if (goalCount > 1) "s" else ""} active",
                        color   = TextWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
            Text("→", color = GreenColor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

// ── App Header ─────────────────────────────────────────────────
@Composable
fun AppHeader() {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A1628), DarkBg)))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.width(220.dp).height(40.dp).clip(RoundedCornerShape(12.dp)).background(ActiveBlue.copy(alpha = 0.07f)))
        Text("LinguaQuest", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite, letterSpacing = 3.sp)
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).width(100.dp).height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(Brush.horizontalGradient(listOf(Color.Transparent, ActiveBlue, Color.Transparent)))
        )
    }
}

// ── Stats Card ─────────────────────────────────────────────────
@Composable
fun StatsCard() {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape     = RoundedCornerShape(32.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFF0A1628)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            StatItem("🇰🇷", "10",  TextWhite)
            StatItem("🔥",  "2",   Color(0xFFFF9800))
            StatItem("💎",  "140", Color(0xFF4DD0E1))
            StatItem("⚡",  "25",  Color(0xFFCE93D8))
        }
    }
}

@Composable
fun StatItem(icon: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 20.sp)
        Spacer(Modifier.width(4.dp))
        Text(value, color = color, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
    }
}

// ── Section Banner Card ────────────────────────────────────────
@Composable
fun SectionBannerCard() {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(BannerBlue, BannerDark)))
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("SECTION 2, UNIT 1", color = Color(0xFFBBDEFB), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Find your way at the airport", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Box(modifier = Modifier.width(170.dp).height(7.dp).clip(RoundedCornerShape(4.dp)).background(Color(0x44000000))) {
                        Box(modifier = Modifier.fillMaxHeight().width(55.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFBBDEFB)))
                    }
                }
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                        .background(Color(0x44000000)).border(1.dp, Color(0x6664B5F6), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("📋", fontSize = 22.sp) }
            }
        }
    }
}

// ── Welcome Banner ─────────────────────────────────────────────
@Composable
fun WelcomeBanner(name: String) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF0D3B2E), Color(0xFF0A4A3A))))
                .border(1.dp, GreenColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🇰🇷", fontSize = 28.sp)
                Column {
                    Text("안녕하세요, $name! 👋", color = GreenColor, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Welcome back! Ready to learn Korean?", color = TextWhite.copy(alpha = 0.75f), fontSize = 12.sp)
                }
            }
        }
    }
}

// ── Lesson Path ────────────────────────────────────────────────
@Composable
fun LessonPath(onGoClick: (String) -> Unit) {
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
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        lessons.forEachIndexed { index, lesson ->
            val bubbleOnLeft = index % 2 == 0
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(if (index % 2 == 0) "✦" else "✧", fontSize = 6.sp, color = Color.White.copy(alpha = 0.25f), modifier = Modifier.align(Alignment.CenterStart).padding(start = 6.dp))
                Text(if (index % 2 == 0) "✧" else "✦", fontSize = 6.sp, color = Color.White.copy(alpha = 0.25f), modifier = Modifier.align(Alignment.CenterEnd).padding(end = 6.dp))
                ZigzagRow(lesson = lesson, bubbleOnLeft = bubbleOnLeft, onGoClick = { onGoClick("${lesson.unit} – ${lesson.topic.replace("\n", " ")}") })
            }
            if (index < lessons.size - 1) DiagonalArrow(fromLeft = bubbleOnLeft, toLeft = (index + 1) % 2 == 0, isActivePath = lesson.isActive)
        }
    }
}

@Composable
fun ZigzagRow(lesson: LessonNode, bubbleOnLeft: Boolean, onGoClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (bubbleOnLeft) Arrangement.Start else Arrangement.End
    ) {
        if (bubbleOnLeft) {
            Spacer(Modifier.width(24.dp))
            BubbleCircle(lesson = lesson, onGoClick = onGoClick)
            Spacer(Modifier.width(14.dp))
            if (lesson.isActive) ExpandableLessonCard(lesson) else LessonLabel(lesson, alignEnd = false)
        } else {
            if (lesson.isActive) ExpandableLessonCard(lesson) else LessonLabel(lesson, alignEnd = true)
            Spacer(Modifier.width(14.dp))
            BubbleCircle(lesson = lesson, onGoClick = onGoClick)
            Spacer(Modifier.width(24.dp))
        }
    }
}

@Composable
fun ExpandableLessonCard(lesson: LessonNode) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier  = Modifier.width(140.dp).clickable { expanded = !expanded }
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFF0D2235)),
        border    = BorderStroke(1.dp, ActiveBlue.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(ActiveBlue.copy(alpha = 0.18f)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                Text(lesson.unit, color = ActiveBlue, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
            }
            Text(lesson.topic, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 16.sp)
            Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(GoldColor.copy(alpha = 0.15f)).padding(horizontal = 7.dp, vertical = 3.dp)) {
                Text("⭐ +10 XP", color = GoldColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            if (expanded) {
                Spacer(Modifier.height(6.dp))
                HorizontalDivider(color = ActiveBlue.copy(alpha = 0.2f), thickness = 1.dp)
                Spacer(Modifier.height(6.dp))
                Text("✈️  공항 = Airport",           color = TextWhite,                    fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("🗣️  어디에요? = Where is it?", color = TextWhite.copy(alpha = 0.85f), fontSize = 12.sp)
                Text("🎫  표 = Ticket",              color = TextWhite.copy(alpha = 0.85f), fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("📚", fontSize = 11.sp)
                    Text("3 exercises  •  ~5 mins", color = TextMuted, fontSize = 10.sp)
                }
                Spacer(Modifier.height(4.dp))
                Text("Tap GO to start! 🚀", color = GreenColor.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BubbleCircle(lesson: LessonNode, onGoClick: () -> Unit) {
    Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.size(64.dp).offset(y = 6.dp).clip(CircleShape).background(if (lesson.isActive) ActiveShadow else LockedShadow))
        Box(
            modifier = Modifier.size(64.dp).clip(CircleShape)
                .background(if (lesson.isActive) Brush.radialGradient(listOf(Color(0xFF50D8FF), ActiveBlue, Color(0xFF0A90C8))) else Brush.radialGradient(listOf(Color(0xFF1E3045), LockedFill)))
                .border(if (lesson.isActive) 3.dp else 2.dp, if (lesson.isActive) ActiveRing else LockedRing, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.size(20.dp).offset(x = (-11).dp, y = (-11).dp).clip(CircleShape).background(Color.White.copy(alpha = if (lesson.isActive) 0.28f else 0.07f)))
            Text(lesson.icon, fontSize = 24.sp, textAlign = TextAlign.Center)
        }
        if (!lesson.isActive) {
            Box(
                modifier = Modifier.size(20.dp).align(Alignment.BottomEnd).offset(x = 2.dp, y = 2.dp)
                    .clip(CircleShape).background(DarkBg).border(1.dp, LockedRing, CircleShape),
                contentAlignment = Alignment.Center
            ) { Text("🔒", fontSize = 8.sp, textAlign = TextAlign.Center) }
        }
        if (lesson.isActive) {
            val goInteraction = remember { MutableInteractionSource() }
            val goPressed by goInteraction.collectIsPressedAsState()
            Box(
                modifier = Modifier.align(Alignment.BottomCenter).offset(y = 10.dp)
                    .scale(if (goPressed) 0.92f else 1f).clip(RoundedCornerShape(10.dp))
                    .background(if (goPressed) GreenColor.copy(alpha = 0.75f) else GreenColor)
                    .clickable(interactionSource = goInteraction, indication = ripple(color = Color.White)) { onGoClick() }
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) { Text("GO", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold) }
        }
    }
}

@Composable
fun LessonLabel(lesson: LessonNode, alignEnd: Boolean) {
    Column(modifier = Modifier.width(110.dp), horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start) {
        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(LockedRing.copy(alpha = 0.12f)).padding(horizontal = 8.dp, vertical = 3.dp)) {
            Text(lesson.unit, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(lesson.topic, color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textAlign = if (alignEnd) TextAlign.End else TextAlign.Start, lineHeight = 16.sp)
    }
}

@Composable
fun DiagonalArrow(fromLeft: Boolean, toLeft: Boolean, isActivePath: Boolean) {
    val pawAngle   = if (fromLeft) 135f else 225f
    val arrowColor = Color.White.copy(alpha = if (isActivePath) 0.60f else 0.22f)
    val leftX = 76.dp; val rightX = 268.dp
    val startX = if (fromLeft) leftX else rightX
    val endX   = if (fromLeft) rightX else leftX
    Box(modifier = Modifier.fillMaxWidth().height(90.dp)) {
        repeat(5) { i ->
            val fraction = i.toFloat() / 4f
            Box(modifier = Modifier.offset(x = startX + (endX - startX) * fraction - 12.dp, y = (62.dp * fraction))) {
                Text("🐾", fontSize = 22.sp, color = arrowColor, modifier = Modifier.rotate(pawAngle))
            }
        }
    }
}

// ── Bottom Nav Bar ─────────────────────────────────────────────
@Composable
fun BottomNavBar(activeNav: String, onNavSelect: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().background(NavBg).padding(vertical = 10.dp, horizontal = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            NavItem("🏠", "Home",        activeNav == "Home")        { onNavSelect("Home") }
            NavItem("🏆", "Leaderboard", activeNav == "Leaderboard") { onNavSelect("Leaderboard") }
            NavItem("🎯", "Goals",       activeNav == "Goals")       { onNavSelect("Goals") }
            NavItem("💬", "Chat",        activeNav == "Chat")        { onNavSelect("Chat") }
            NavItem("👤", "Profile",     activeNav == "Profile")     { onNavSelect("Profile") }
        }
    }
}

@Composable
fun NavItem(icon: String, label: String, isActive: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clip(RoundedCornerShape(14.dp))
            .background(when { isActive -> Color(0xFF1A3050); pressed -> Color.White.copy(alpha = 0.06f); else -> Color.Transparent })
            .border(if (isActive) 1.dp else 0.dp, if (isActive) ActiveBlue.copy(0.35f) else Color.Transparent, RoundedCornerShape(14.dp))
            .clickable(interactionSource = interactionSource, indication = ripple(color = ActiveBlue)) { onClick() }
            .scale(if (pressed) 0.93f else 1f)
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Text(icon, fontSize = 22.sp)
        Spacer(Modifier.height(2.dp))
        Text(label, color = if (isActive) ActiveBlue else TextMuted, fontSize = 9.sp, fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Normal)
    }
}