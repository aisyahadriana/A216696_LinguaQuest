package com.example.a216696_wan_project2

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ── Goal DAO ───────────────────────────────────────────────────
// DAO = Data Access Object.
// This interface defines how we talk to the "goals" table.
// Room automatically generates the actual implementation at compile time.
@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity)

    @Query("SELECT * FROM goals ORDER BY id DESC")
    fun getAll(): Flow<List<GoalEntity>>

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteById(id: Int)

    // UPDATE the isCompleted status of a goal
    @Query("UPDATE goals SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompleted(id: Int, isCompleted: Boolean)

    // DELETE all goals (used for reset)
    @Query("DELETE FROM goals")
    suspend fun deleteAll()
}
