package com.example.chatroomapp.data

import java.sql.Timestamp

data class Message (
    val id: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderFirstName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSentByCurrentUser: Boolean = false
)