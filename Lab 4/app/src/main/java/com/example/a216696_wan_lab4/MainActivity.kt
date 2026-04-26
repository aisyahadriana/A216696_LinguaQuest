package com.example.a216696_wan_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.a216696_wan_lab4.ui.theme.A216696_Wan_Lab4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216696_Wan_Lab4Theme {
                AppNavigation()
            }
        }
    }
}

// ── Preview helper ─────────────────────────────────────────────
private fun previewViewModel(
    name: String = "Eleanor",
    lesson: String = ""
): UserViewModel = UserViewModel().apply {
    setUserName(name)
    if (lesson.isNotEmpty()) setCurrentLesson(lesson)
}

// ── Screen 1: Home ─────────────────────────────────────────────
@Preview(name = "Home – no name", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewHomeEmpty() {
    A216696_Wan_Lab4Theme {
        MainScreen(
            viewModel         = previewViewModel(name = ""),
            onGoToLesson      = {},
            onGoToProfile     = {},
            onGoToLeaderboard = {}
        )
    }
}

@Preview(name = "Home – with name", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewHomeWithName() {
    A216696_Wan_Lab4Theme {
        MainScreen(
            viewModel         = previewViewModel(name = "Eleanor"),
            onGoToLesson      = {},
            onGoToProfile     = {},
            onGoToLeaderboard = {}
        )
    }
}

// ── Screen 2: Lesson Detail ────────────────────────────────────
@Preview(name = "Lesson Detail", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLessonDetail() {
    A216696_Wan_Lab4Theme {
        LessonDetailScreen(
            viewModel = previewViewModel(
                name   = "Eleanor",
                lesson = "Unit 1 – Find Your Way at the Airport"
            ),
            onBack = {}
        )
    }
}

// ── Screen 3: Profile ──────────────────────────────────────────
@Preview(name = "Profile – no lesson", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewProfileNoLesson() {
    A216696_Wan_Lab4Theme {
        ProfileScreen(
            viewModel = previewViewModel(name = "Eleanor"),
            onBack    = {}
        )
    }
}

@Preview(name = "Profile – with lesson", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewProfileWithLesson() {
    A216696_Wan_Lab4Theme {
        ProfileScreen(
            viewModel = previewViewModel(
                name   = "Eleanor",
                lesson = "Unit 1 – Find Your Way at the Airport"
            ),
            onBack = {}
        )
    }
}

// ── Screen 4: Leaderboard ──────────────────────────────────────
@Preview(name = "Leaderboard", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLeaderboard() {
    A216696_Wan_Lab4Theme {
        LeaderboardScreen(
            viewModel = previewViewModel(name = "Eleanor"),
            onBack    = {}
        )
    }
}