package com.example.a216696_wan_lab5

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UserData(
    val userName: String = "",
    val currentLesson: String = ""
)

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository   = AppRepository(database.goalDao())
    }

    // In-memory only — no Room needed for these
    var userData by mutableStateOf(UserData())
        private set

    fun setUserName(name: String) {
        userData = userData.copy(userName = name)
    }

    fun setCurrentLesson(lesson: String) {
        userData = userData.copy(currentLesson = lesson)
    }

    // Goals from Room — persists after app restart
    val studyGoals: StateFlow<List<GoalEntity>> = repository.allGoals.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addGoal(title: String, targetDays: Int) {
        viewModelScope.launch {
            repository.insertGoal(GoalEntity(title = title, targetDays = targetDays))
        }
    }

    fun toggleGoal(id: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleGoal(id, !currentStatus)
        }
    }

    fun deleteGoal(id: Int) {
        viewModelScope.launch {
            repository.deleteGoal(id)
        }
    }

    fun resetProgress() {
        userData = UserData()
        viewModelScope.launch {
            repository.deleteAllGoals()
        }
    }
}