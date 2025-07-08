package com.example.chatroomapp.data

import com.example.chatroomapp.data.Result
import com.example.chatroomapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val auth: FirebaseAuth,
                     private val firestore: FirebaseFirestore

){
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<Boolean> =
    try {
        auth.createUserWithEmailAndPassword(email, password).await()
        val user = User(firstName,lastName,email)
        saveUserToFirestore(user)
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
    } catch (e: Exception) {
            Result.Error(e)
        }
    suspend fun getUserByEmail(email: String): User? {
        val snapshot = firestore.collection("users").document(email).get().await()
        return snapshot.toObject(User::class.java)
    }
}