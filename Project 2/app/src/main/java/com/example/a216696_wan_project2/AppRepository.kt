package com.example.a216696_wan_project2

import kotlinx.coroutines.flow.Flow

// Repository connects ViewModel ↔ DAO
// ViewModel never talks to DAO directly — always through here
class AppRepository(private val goalDao: GoalDao) {
    val allGoals: Flow<List<GoalEntity>> = goalDao.getAll()
    suspend fun insertGoal(goal: GoalEntity)               = goalDao.insert(goal)
    suspend fun deleteGoal(id: Int)                        = goalDao.deleteById(id)
    suspend fun toggleGoal(id: Int, isCompleted: Boolean)  = goalDao.updateCompleted(id, isCompleted)
    suspend fun deleteAllGoals()                           = goalDao.deleteAll()
}
