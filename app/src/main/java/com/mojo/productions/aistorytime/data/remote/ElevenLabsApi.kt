package com.mojo.productions.aistorytime.data.remote

import com.mojo.productions.aistorytime.BuildConfig
import com.mojo.productions.aistorytime.data.remote.models.TextToSpeechRequest
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ElevenLabsApi {

  @Streaming
  @POST("text-to-speech/{voice_id}/stream")
  suspend fun getTextToSpeechResponse(
    @Path("voice_id") voiceId: String,
    @Body request: TextToSpeechRequest
  ): ResponseBody

  class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
      chain.request()
        .newBuilder()
        .addHeader("xi-api-key", BuildConfig.ELEVEN_LABS_API_KEY)
        .build()
        .let(chain::proceed)
  }
}