package com.example.a216696_wan_lab4

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ── Route constants ────────────────────────────────────────────
object Routes {
    const val HOME          = "home"
    const val LESSON_DETAIL = "lesson_detail"
    const val PROFILE       = "profile"
    const val LEADERBOARD   = "leaderboard"
}

// ── App Navigation Host ────────────────────────────────────────
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: UserViewModel         = viewModel()
) {
    NavHost(
        navController    = navController,
        startDestination = Routes.HOME
    ) {

        // ── Screen 1: Home ─────────────────────────────────────
        composable(Routes.HOME) {
            MainScreen(
                viewModel     = viewModel,
                onGoToLesson  = { lessonTitle ->
                    viewModel.setCurrentLesson(lessonTitle)
                    navController.navigate(Routes.LESSON_DETAIL)
                },
                onGoToProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onGoToLeaderboard = {
                    navController.navigate(Routes.LEADERBOARD)
                }
            )
        }

        // ── Screen 2: Lesson Detail ─────────────────────────────
        composable(Routes.LESSON_DETAIL) {
            LessonDetailScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }

        // ── Screen 3: Profile ───────────────────────────────────
        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }

        // ── Screen 4: Leaderboard ───────────────────────────────
        composable(Routes.LEADERBOARD) {
            LeaderboardScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}