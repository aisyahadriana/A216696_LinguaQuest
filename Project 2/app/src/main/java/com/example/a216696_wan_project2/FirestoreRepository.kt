package com.example.a216696_wan_project2

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class CommunityPhrase(
    val id:          String = "",
    val korean:      String = "",
    val translation: String = "",
    val submittedBy: String = "Anonymous",
    val targetLang:  String = "ms",
    val timestamp:   Long   = System.currentTimeMillis(),
    val likes:       Int    = 0
)

class FirestoreRepository {

    private val db         = FirebaseFirestore.getInstance()
    private val collection = db.collection("community_phrases")

    suspend fun addPhrase(phrase: CommunityPhrase): Result<String> {
        return try {
            val docRef = collection.add(
                mapOf(
                    "korean"      to phrase.korean,
                    "translation" to phrase.translation,
                    "submittedBy" to phrase.submittedBy,
                    "targetLang"  to phrase.targetLang,
                    "timestamp"   to phrase.timestamp,
                    "likes"       to phrase.likes
                )
            ).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCommunityPhrasesFlow(): Flow<List<CommunityPhrase>> = callbackFlow {
        val listener = collection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val phrases = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        CommunityPhrase(
                            id          = doc.id,
                            korean      = doc.getString("korean")      ?: "",
                            translation = doc.getString("translation") ?: "",
                            submittedBy = doc.getString("submittedBy") ?: "Anonymous",
                            targetLang  = doc.getString("targetLang")  ?: "ms",
                            timestamp   = doc.getLong("timestamp")     ?: 0L,
                            likes       = doc.getLong("likes")?.toInt() ?: 0
                        )
                    } catch (e: Exception) { null }
                } ?: emptyList()
                trySend(phrases)
            }
        awaitClose { listener.remove() }
    }

    suspend fun likePhrase(docId: String): Result<Unit> {
        return try {
            collection.document(docId)
                .update("likes", FieldValue.increment(1))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePhrase(docId: String): Result<Unit> {
        return try {
            collection.document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}