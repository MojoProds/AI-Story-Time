package com.mojo.productions.aistorytime.data.remote

import com.mojo.productions.aistorytime.BuildConfig
import com.mojo.productions.aistorytime.data.remote.models.ChatGptRequest
import com.mojo.productions.aistorytime.data.remote.models.ChatGptResponse
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {

  @POST("chat/completions")
  suspend fun getChatGptResponse(
    @Body request: ChatGptRequest,
  ): ChatGptResponse

  class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
      chain.request()
        .newBuilder()
        .addHeader("Authorization", "Bearer ${BuildConfig.OPEN_AI_API_KEY}")
        .build()
        .let(chain::proceed)
  }
}