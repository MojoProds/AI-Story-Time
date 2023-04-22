package com.mojo.productions.aistorytime.data.remote

import com.mojo.productions.aistorytime.util.Constants.ELEVEN_LABS_BASE_URL
import com.mojo.productions.aistorytime.util.Constants.OPEN_AI_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

  @Singleton
  @Provides
  fun provideOpenAiApi(): OpenAiApi {
    return Retrofit.Builder()
      .baseUrl(OPEN_AI_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(OkHttpClient.Builder().apply {
        addInterceptor(OpenAiApi.HeaderInterceptor())
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
      }.build())
      .build().create(OpenAiApi::class.java)
  }

  @Singleton
  @Provides
  fun provideElevenLabsApi(): ElevenLabsApi {
    return Retrofit.Builder()
      .baseUrl(ELEVEN_LABS_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(OkHttpClient.Builder().apply {
        addInterceptor(ElevenLabsApi.HeaderInterceptor())
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
      }.build())
      .build().create(ElevenLabsApi::class.java)
  }
}