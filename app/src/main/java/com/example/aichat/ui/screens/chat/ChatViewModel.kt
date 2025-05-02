package com.example.aichat.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ChatViewModel @Inject constructor() : ViewModel() {
    
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
            val userMessage = Message(
                id = UUID.randomUUID().toString(),
                content = message,
                isFromUser = true
            )
            
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + userMessage,
                    currentMessage = "",
                    isLoading = true
                )
            }
            
            // TODO: Implementar la llamada a la API de OpenAI
            // Por ahora, simularemos una respuesta después de un breve delay
            // Esto se reemplazará más adelante con la respuesta real de la API
            val aiMessage = Message(
                id = UUID.randomUUID().toString(),
                content = "Esta es una respuesta temporal. La integración con OpenAI se implementará más adelante.",
                isFromUser = false
            )
            
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + aiMessage,
                    isLoading = false
                )
            }
        }
    }
}

data class ChatUiState(
    val currentMessage: String = "",
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
) 