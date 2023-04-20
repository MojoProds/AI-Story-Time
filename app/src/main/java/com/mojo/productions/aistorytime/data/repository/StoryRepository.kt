package com.mojo.productions.aistorytime.data.repository

import com.mojo.productions.aistorytime.data.model.Paragraph
import com.mojo.productions.aistorytime.data.model.Story
import com.mojo.productions.aistorytime.data.remote.OpenAiApi
import com.mojo.productions.aistorytime.data.remote.models.ChatGptRequest
import com.mojo.productions.aistorytime.data.remote.models.ChatGptResponse
import com.mojo.productions.aistorytime.data.remote.models.Message
import com.mojo.productions.aistorytime.util.LoadResult
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

const val CHAT_GPT_MODEL = "gpt-3.5-turbo"
const val CHAT_GPT_PROMPT_PREFIX = "Tell me a story about "
val CHAT_GPT_INSTRUCTION = Message("system", "You are narrator for children's books.")

@ActivityScoped
class StoryRepository @Inject constructor(
  private val openAiApi: OpenAiApi,
) {

  suspend fun getStory(prompt: String): LoadResult<Story> {
    val response = try {
      createStory(openAiApi.getChatGptResponse(createChatGptRequest(prompt)))
    } catch (e: Exception) {
      return LoadResult.Error("An unknown error occurred.")
    }
    return LoadResult.Success(response)
  }

  private fun createChatGptRequest(prompt: String): ChatGptRequest {
    return ChatGptRequest(
      CHAT_GPT_MODEL,
      listOf(
        CHAT_GPT_INSTRUCTION,
        Message("user", CHAT_GPT_PROMPT_PREFIX + prompt)
      )
    )
  }

  private fun createStory(response: ChatGptResponse): Story {
    return Story(
      response.choices[0].message.content
        .split("\n\n")
        .map { paragraph -> Paragraph(paragraph, "", "") })
  }
}