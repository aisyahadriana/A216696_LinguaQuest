package com.example.a216696_wan_project1

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ── Route constants ────────────────────────────────────────────
// All navigation destinations defined in one place.
// To navigate anywhere: navController.navigate(Routes.GOALS)
object Routes {
    const val HOME          = "home"
    const val LESSON_DETAIL = "lesson_detail"
    const val ADD_GOAL      = "add_goal"       // Screen 3 – form input
    const val GOALS         = "goals"          // Screen 4 – goals list
    const val PROFILE       = "profile"        // Screen 5 – profile
    const val LEADERBOARD   = "leaderboard"    // Screen 6 – bonus
}

// ── App Navigation Host ────────────────────────────────────────
// Creates one NavController and one shared ViewModel,
// then wires every screen to its route.
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: UserViewModel         = viewModel()
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        // Screen 1 – Home
        composable(Routes.HOME) {
            MainScreen(
                viewModel         = viewModel,
                onGoToLesson      = { lesson ->
                    viewModel.setCurrentLesson(lesson)
                    navController.navigate(Routes.LESSON_DETAIL)
                },
                onGoToProfile     = { navController.navigate(Routes.PROFILE) },
                onGoToLeaderboard = { navController.navigate(Routes.LEADERBOARD) },
                onGoToGoals       = { navController.navigate(Routes.GOALS) }
            )
        }

        // Screen 2 – Lesson Detail
        composable(Routes.LESSON_DETAIL) {
            LessonDetailScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }

        // Screen 3 – Add Goal (Form Input)
        composable(Routes.ADD_GOAL) {
            AddGoalScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() },
                onGoalAdded = {
                    // After adding, go straight to Goals list so user sees it
                    navController.navigate(Routes.GOALS) {
                        popUpTo(Routes.ADD_GOAL) { inclusive = true }
                    }
                }
            )
        }

        // Screen 4 – Goals List
        composable(Routes.GOALS) {
            GoalsScreen(
                viewModel    = viewModel,
                onBack       = { navController.popBackStack() },
                onAddNewGoal = { navController.navigate(Routes.ADD_GOAL) }
            )
        }

        // Screen 5 – Profile
        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }

        // Screen 6 – Leaderboard (bonus)
        composable(Routes.LEADERBOARD) {
            LeaderboardScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}