package com.example.a216696_wan_project2

import androidx.room.Entity
import androidx.room.PrimaryKey

// ── Goal Entity ────────────────────────────────────────────────
// @Entity tells Room to create a table called "goals" in the database.
// Each field becomes a column in the table.
// Data saved here PERSISTS even after the app is closed and reopened.
@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val targetDays: Int,
    val isCompleted: Boolean = false
)
