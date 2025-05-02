package com.example.aichat.domain.model

data class Message(
    val id: String = "",
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 