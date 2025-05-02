package com.example.aichat.ui.screens.chat

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        
        // Por ahora solo limpiamos el mensaje, más adelante implementaremos
        // la lógica de envío a la API y guardado en base de datos
        _uiState.value = _uiState.value.copy(
            currentMessage = ""
        )
    }
}

data class ChatUiState(
    val currentMessage: String = "",
    val isLoading: Boolean = false
) 