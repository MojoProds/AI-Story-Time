package com.mojo.productions.aistorytime.di

import com.mojo.productions.aistorytime.data.remote.ChatGptApi
import com.mojo.productions.aistorytime.repository.StoryRepository
import com.mojo.productions.aistorytime.util.Constants.OPEN_AI_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun provideStoryRepository(
    chatGptApi: ChatGptApi,
  ) = StoryRepository(chatGptApi)

  @Singleton
  @Provides
  fun provideChatGptApi(): ChatGptApi {
    return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
      .baseUrl(OPEN_AI_BASE_URL)
      .build().create(ChatGptApi::class.java);
  }
}