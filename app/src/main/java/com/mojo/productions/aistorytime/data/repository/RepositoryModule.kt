package com.mojo.productions.aistorytime.data.repository

import android.content.Context
import com.mojo.productions.aistorytime.data.remote.ElevenLabsApi
import com.mojo.productions.aistorytime.data.remote.OpenAiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

  @Singleton
  @Provides
  fun provideStoryRepository(
    @ApplicationContext applicationContext: Context,
    openAiApi: OpenAiApi,
    elevenLabsApi: ElevenLabsApi
  ) = StoryRepository(applicationContext, openAiApi, elevenLabsApi)
}