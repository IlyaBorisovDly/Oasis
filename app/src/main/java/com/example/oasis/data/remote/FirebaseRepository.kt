package com.example.oasis.data.remote

import android.util.Log
import com.example.oasis.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseRepository {

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

}