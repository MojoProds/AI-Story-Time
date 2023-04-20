package com.mojo.productions.aistorytime.data.remote

import com.mojo.productions.aistorytime.data.remote.responses.ChatGptResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatGptApi {

  @POST("chat/completions")
  suspend fun getChatGptResponse(
    @Query("prompt") prompt: String,
  ): ChatGptResponse
}