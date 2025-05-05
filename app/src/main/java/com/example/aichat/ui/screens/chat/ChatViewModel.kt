package com.example.aichat.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aichat.data.ChatRepository
import com.example.aichat.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    fun onMessageChange(newMessage: String) {
        _uiState.update { currentState ->
            currentState.copy(currentMessage = newMessage)
        }
    }
    
    fun sendMessage(message: String) {
        if (message.isBlank()) return
        
        viewModelScope.launch {
            try {
                val userMessage = Message(
                    id = UUID.randomUUID().toString(),
                    content = message,
                    isFromUser = true
                )
                
                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + userMessage,
                        currentMessage = "",
                        isLoading = true,
                        error = null
                    )
                }
                
                chatRepository.getAIResponse(_uiState.value.messages).collect { response ->
                    val aiMessage = Message(
                        id = UUID.randomUUID().toString(),
                        content = response,
                        isFromUser = false
                    )
                    
                    _uiState.update { currentState ->
                        currentState.copy(
                            messages = currentState.messages + aiMessage,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error al enviar mensaje", e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "Error al enviar el mensaje: ${e.message}"
                    )
                }
            }
        }
    }
}

data class ChatUiState(
    val currentMessage: String = "",
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 