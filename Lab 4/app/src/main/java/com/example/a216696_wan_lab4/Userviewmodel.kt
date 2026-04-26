package com.example.a216696_wan_lab4

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// ── Data Class ─────────────────────────────────────────────────
// Holds the user's info: name and which lesson they are currently on
data class UserData(
    val userName: String = "",
    val currentLesson: String = ""
)

// ── ViewModel ──────────────────────────────────────────────────
class UserViewModel : ViewModel() {

    var userData by mutableStateOf(UserData())
        private set

    // Called when the user submits the start-lesson dialog
    fun setUserName(name: String) {
        userData = userData.copy(userName = name)
    }

    // Called when navigation moves to a lesson detail screen
    fun setCurrentLesson(lesson: String) {
        userData = userData.copy(currentLesson = lesson)
    }

    // Reset progress (used on Profile screen)
    fun resetProgress() {
        userData = UserData()
    }
}