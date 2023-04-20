package com.mojo.productions.aistorytime.repository

import com.mojo.productions.aistorytime.data.remote.ChatGptApi
import com.mojo.productions.aistorytime.data.remote.responses.ChatGptResponse
import com.mojo.productions.aistorytime.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class StoryRepository @Inject constructor(
  private val chatGptApi: ChatGptApi,
) {

  suspend fun getChatGptResponse(prompt: String): Resource<ChatGptResponse> {
    val response = try {
      chatGptApi.getChatGptResponse(prompt)
    } catch (e: Exception) {
      return Resource.Error("An unknown error occurred.")
    }
    return Resource.Success(response);
  }
}