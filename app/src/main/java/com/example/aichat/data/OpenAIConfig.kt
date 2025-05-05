package com.example.aichat.data

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.example.aichat.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenAIConfig {
    
    @Provides
    @Singleton
    fun provideOpenAI(): OpenAI {
        return OpenAI(
            config = OpenAIConfig(
                token = BuildConfig.OPENAI_API_KEY
            )
        )
    }
    
    @Provides
    @Singleton
    fun provideChatCompletionRequest(messages: List<ChatMessage>): ChatCompletionRequest {
        return ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = messages
        )
    }
} 