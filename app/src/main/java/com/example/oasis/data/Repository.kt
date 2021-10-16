package com.example.oasis.data

import android.util.Log
import com.example.oasis.State
import com.example.oasis.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

object Repository {

    fun addUser(id : String, user: User) {
        val db = Firebase.firestore

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

    fun getBestResults() = flow {

        emit(State.loading())

        val db = Firebase.firestore
        val id = Firebase.auth.currentUser!!.uid

        val task = db.collection("users").document(id).get().await()
        val data = task.data!!["bestResults"] as Map<String, Int>

        emit(State.success(data))
    }.catch {
        emit(State.failed("Repository: Failed to get best results"))
    }.flowOn(Dispatchers.IO)
}