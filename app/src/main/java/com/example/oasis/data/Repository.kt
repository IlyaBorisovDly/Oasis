package com.example.oasis.data

import android.util.Log
import com.example.oasis.State
import com.example.oasis.model.User
import com.example.oasis.WorkoutType
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class Repository {

    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser
    private val id = currentUser?.uid ?: throw java.lang.NullPointerException("Repository: user is null")

    fun addUser(id : String, user: User) {

        db.collection("users")
            .document(id)
            .set(user)
            .addOnSuccessListener {
                Log.d("firebase", "addUser: success")
            }
            .addOnFailureListener {
                Log.d("firebase", "addUser: failure")
            }
    }

    suspend fun updateBestResults(map: Map<String, Int>, workoutType: WorkoutType) {
        val task = db.collection("users").document(id).get().await()

        val reference = when (workoutType) {
            WorkoutType.FIRST  -> "bestResults1"
            WorkoutType.SECOND -> "bestResults2"
            WorkoutType.THIRD  -> "bestResults3"
        }

        val data = task.data?.get(reference) as MutableMap<String, Int>
        Log.d("firebase", "updateBestResults: got data $data")

        map.forEach {
            data[it.key] = it.value
        }
        Log.d("firebase", "updateBestResults: Start updating")

        db.collection("users")
            .document(id)
            .update(reference, data)
            .addOnSuccessListener {
                Log.d("firebase", "updateBestResults: Success")
            }
            .addOnFailureListener {
                Log.d("firebase", "updateBestResults: Failure")
            }
    }

    fun getBestResults(workoutType: WorkoutType) = flow {

        emit(State.loading())

        val task = db.collection("users").document(id).get().await()

        val reference =
            when (workoutType) {
                WorkoutType.FIRST  -> "bestResults1"
                WorkoutType.SECOND -> "bestResults2"
                WorkoutType.THIRD  -> "bestResults3"
        }

        val data = task.data?.get(reference) as MutableMap<String, Int>

        emit(State.success(data))

    }.catch {
        emit(State.failed("Repository: Failed to get best results"))
    }.flowOn(Dispatchers.IO)
}