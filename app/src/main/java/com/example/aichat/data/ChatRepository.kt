package com.example.aichat.data

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.client.OpenAI
import com.example.aichat.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val openAI: OpenAI
) {
    suspend fun getAIResponse(messages: List<Message>): Flow<String> = flow {
        try {
            Log.d("ChatRepository", "Preparando mensajes para OpenAI")
            val chatMessages = messages.map { message ->
                ChatMessage(
                    role = if (message.isFromUser) ChatRole.User else ChatRole.Assistant,
                    content = message.content
                )
            }
            
            Log.d("ChatRepository", "Enviando solicitud a OpenAI")
            val request = ChatCompletionRequest(
                model = com.aallam.openai.api.model.ModelId("gpt-3.5-turbo"),
                messages = chatMessages
            )
            
            val response = openAI.chatCompletion(request)
            val aiResponse = response.choices.firstOrNull()?.message?.content
            
            if (aiResponse != null) {
                Log.d("ChatRepository", "Respuesta recibida de OpenAI: $aiResponse")
                emit(aiResponse)
            } else {
                Log.e("ChatRepository", "La respuesta de OpenAI está vacía")
                emit("Lo siento, no pude generar una respuesta. Por favor, inténtalo de nuevo.")
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error al procesar la solicitud", e)
            when (e) {
                is com.aallam.openai.api.exception.OpenAIException -> {
                    emit("Error de OpenAI: ${e.message}")
                }
                else -> {
                    emit("Lo siento, ha ocurrido un error al procesar tu mensaje. Por favor, inténtalo de nuevo.")
                }
            }
        }
    }
} 