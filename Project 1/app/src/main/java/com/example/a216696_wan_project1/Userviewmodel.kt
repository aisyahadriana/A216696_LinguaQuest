package com.example.a216696_wan_project1

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// ── Data Classes ───────────────────────────────────────────────

// Stores basic user info shared across all screens
data class UserData(
    val userName: String = "",
    val currentLesson: String = ""
)

// Represents one study goal added by the user
data class StudyGoal(
    val id: Int,
    val title: String,        // e.g. "Learn 10 words today"
    val targetDays: Int,      // how many days to complete
    val isCompleted: Boolean = false
)

// ── ViewModel ──────────────────────────────────────────────────
// ONE ViewModel shared across ALL screens via AppNavigation.
class UserViewModel : ViewModel() {

    // ── User info state ────────────────────────────────────────
    var userData by mutableStateOf(UserData())
        private set

    // ── Goals list state ───────────────────────────────────────
    // mutableStateOf on a List — Compose observes this and recomposes
    // any screen reading it whenever the list changes.
    var studyGoals by mutableStateOf<List<StudyGoal>>(emptyList())
        private set

    // Auto-increment ID counter for goals
    private var nextGoalId = 1

    // ── User info functions ────────────────────────────────────

    fun setUserName(name: String) {
        userData = userData.copy(userName = name)
    }

    fun setCurrentLesson(lesson: String) {
        userData = userData.copy(currentLesson = lesson)
    }

    // ── Goal functions ─────────────────────────────────────────
    // called from AddGoalScreen
    fun addGoal(title: String, targetDays: Int) {
        val newGoal = StudyGoal(
            id         = nextGoalId++,
            title      = title,
            targetDays = targetDays
        )
        studyGoals = studyGoals + newGoal   // creates a new list → triggers recomposition
    }

    // Toggle a goal's completed status — called from GoalsScreen
    fun toggleGoal(id: Int) {
        studyGoals = studyGoals.map { goal ->
            if (goal.id == id) goal.copy(isCompleted = !goal.isCompleted) else goal
        }
    }

    // Delete a goal — called from GoalsScreen
    fun deleteGoal(id: Int) {
        studyGoals = studyGoals.filter { it.id != id }
    }

    // Reset everything
    fun resetProgress() {
        userData    = UserData()
        studyGoals  = emptyList()
        nextGoalId  = 1
    }
}