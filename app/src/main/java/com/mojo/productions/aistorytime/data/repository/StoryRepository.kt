package com.mojo.productions.aistorytime.data.repository

import android.content.Context
import com.mojo.productions.aistorytime.data.model.Paragraph
import com.mojo.productions.aistorytime.data.model.Story
import com.mojo.productions.aistorytime.data.remote.ElevenLabsApi
import com.mojo.productions.aistorytime.data.remote.OpenAiApi
import com.mojo.productions.aistorytime.data.remote.models.ChatGptRequest
import com.mojo.productions.aistorytime.data.remote.models.ChatGptResponse
import com.mojo.productions.aistorytime.data.remote.models.ImageRequest
import com.mojo.productions.aistorytime.data.remote.models.Message
import com.mojo.productions.aistorytime.data.remote.models.Size
import com.mojo.productions.aistorytime.data.remote.models.TextToSpeechRequest
import com.mojo.productions.aistorytime.data.remote.models.VoiceSettings
import com.mojo.productions.aistorytime.util.LoadResult
import dagger.hilt.android.scopes.ActivityScoped
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import okhttp3.ResponseBody


const val CHAT_GPT_MODEL = "gpt-3.5-turbo"
const val CHAT_GPT_PROMPT_PREFIX = "Tell me a story about "
val CHAT_GPT_INSTRUCTION =
  Message("system", "You are narrator for children's stories. Do not ask follow up questions.")

const val SPEECH_VOICE_ID_BELLA = "EXAVITQu4vr4xnSDxMaL"
const val SPEECH_STABILITY = 0.3f
const val SPEECH_BOOST = 0.75f

const val IMAGE_PROMPT_PREFIX = "Illustrate in the style of children's books. "
const val IMAGE_COUNT = 1


@ActivityScoped
class StoryRepository @Inject constructor(
  private val appContext: Context,
  private val openAiApi: OpenAiApi,
  private val elevenLabsApi: ElevenLabsApi,
) {

  suspend fun getStory(prompt: String): LoadResult<Story> {
    val response = try {
      createStory(openAiApi.getChatGptResponse(createChatGptRequest(prompt)))
    } catch (e: Exception) {
      return LoadResult.Error("$e")
    }
    return LoadResult.Success(response)
  }

  suspend fun getVoiceOver(content: String): LoadResult<File> {
    val response = try {
      writeResponseBodyToCache(
        elevenLabsApi.getTextToSpeechResponse(
          SPEECH_VOICE_ID_BELLA,
          createTextToSpeechRequest(
            content
          )
        )
      )
    } catch (e: Exception) {
      return LoadResult.Error("$e")
    }
    return if (response == null) LoadResult.Error("File error occurred.") else LoadResult.Success(
      response
    )
  }

  suspend fun getImage(content: String): LoadResult<String> {
    val response = try {
      openAiApi.getImageResponse(createImageRequest(content)).data[0].url
    } catch (e: Exception) {
      return LoadResult.Error("$e")
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

  private fun createTextToSpeechRequest(content: String): TextToSpeechRequest {
    return TextToSpeechRequest(
      content, VoiceSettings(SPEECH_STABILITY, SPEECH_BOOST)
    )
  }

  private fun writeResponseBodyToCache(body: ResponseBody): File? {
    try {
      val audioFile = File(appContext.cacheDir, "tempAudio.mp3")
      var inputStream: InputStream? = null
      var outputStream: OutputStream? = null
      try {
        val fileReader = ByteArray(4096)
        var fileSizeDownloaded: Long = 0
        inputStream = body.byteStream()
        outputStream = FileOutputStream(audioFile)
        while (true) {
          val read = inputStream.read(fileReader)
          if (read == -1) {
            break
          }
          outputStream.write(fileReader, 0, read)
          fileSizeDownloaded += read.toLong()
        }
        outputStream.flush()
        return audioFile
      } catch (e: IOException) {
        return null
      } finally {
        inputStream?.close()
        outputStream?.close()
        body.close()
      }
    } catch (e: IOException) {
      return null
    }
  }

  private fun createImageRequest(content: String): ImageRequest {
    return ImageRequest(
      IMAGE_PROMPT_PREFIX + content,
      IMAGE_COUNT,
      Size.SIZE_256.size
    )
  }
}