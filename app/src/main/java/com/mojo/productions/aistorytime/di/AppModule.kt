package com.mojo.productions.aistorytime.di

import com.mojo.productions.aistorytime.data.remote.OpenAiApi
import com.mojo.productions.aistorytime.data.repository.StoryRepository
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
object AppModule {

  @Singleton
  @Provides
  fun provideStoryRepository(
    openAiApi: OpenAiApi,
  ) = StoryRepository(openAiApi)

  @Singleton
  @Provides
  fun provideChatGptApi(): OpenAiApi {
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
}