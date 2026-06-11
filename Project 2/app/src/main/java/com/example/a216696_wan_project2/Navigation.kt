package com.example.a216696_wan_project2

// ================================================================
// FILE: Navigation.kt  (UPDATED v2)
// Added: LANGUAGE_SWITCHER route (Screen 10)
// Total screens: 10
// ================================================================

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val HOME             = "home"
    const val LESSON           = "lesson_detail"
    const val ADD_GOAL         = "add_goal"
    const val GOALS            = "goals"
    const val CALCULATOR       = "calculator"
    const val PROFILE          = "profile"
    const val LEADERBOARD      = "leaderboard"
    const val TRANSLATE        = "translate"
    const val COMMUNITY        = "community"
    const val LANGUAGE_SWITCHER = "language_switcher"   // NEW Screen 10
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel:     UserViewModel     = viewModel()
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            MainScreen(
                viewModel          = viewModel,
                onGoToLesson       = { lesson ->
                    viewModel.setCurrentLesson(lesson)
                    navController.navigate(Routes.LESSON)
                },
                onGoToProfile      = { navController.navigate(Routes.PROFILE) },
                onGoToLeaderboard  = { navController.navigate(Routes.LEADERBOARD) },
                onGoToGoals        = { navController.navigate(Routes.GOALS) },
                onGoToCalculator   = { navController.navigate(Routes.CALCULATOR) },
                onGoToTranslate    = { navController.navigate(Routes.TRANSLATE) },
                onGoToCommunity    = { navController.navigate(Routes.COMMUNITY) },
                onGoToLangSwitcher = { navController.navigate(Routes.LANGUAGE_SWITCHER) }
            )
        }

        composable(Routes.LESSON) {
            LessonDetailScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(Routes.ADD_GOAL) {
            AddGoalScreen(
                viewModel   = viewModel,
                onBack      = { navController.popBackStack() },
                onGoalAdded = {
                    navController.navigate(Routes.GOALS) {
                        popUpTo(Routes.ADD_GOAL) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.GOALS) {
            GoalsScreen(
                viewModel    = viewModel,
                onBack       = { navController.popBackStack() },
                onAddNewGoal = { navController.navigate(Routes.ADD_GOAL) }
            )
        }

        composable(Routes.CALCULATOR) {
            ProgressCalculatorScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(Routes.PROFILE) {
            ProfileScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(Routes.LEADERBOARD) {
            LeaderboardScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.TRANSLATE) {
            TranslationScreen(
                viewModel       = viewModel,
                onBack          = { navController.popBackStack() },
                onGoToCommunity = { navController.navigate(Routes.COMMUNITY) }
            )
        }

        composable(Routes.COMMUNITY) {
            CommunityScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(Routes.LANGUAGE_SWITCHER) {
            LanguageSwitcherScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
    }
}