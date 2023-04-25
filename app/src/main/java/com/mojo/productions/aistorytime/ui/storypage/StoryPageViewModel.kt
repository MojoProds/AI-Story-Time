package com.mojo.productions.aistorytime.ui.storypage

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import com.mojo.productions.aistorytime.data.model.Story
import com.mojo.productions.aistorytime.data.repository.StoryRepository
import com.mojo.productions.aistorytime.util.LoadResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@HiltViewModel
class StoryPageViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val repository: StoryRepository,
) : ViewModel() {

  var story = mutableStateOf<Story?>(null)
  var loadError = mutableStateOf("")
  var isLoading = mutableStateOf(false)

  var voiceOverFile = mutableStateOf<File?>(null)

  var imageUrl = mutableStateOf<String?>(null)
  var imageIsLoading = mutableStateOf(false)

  fun loadStory(prompt: String) {
    story.value = null
    viewModelScope.launch {
      isLoading.value = true

      when (val result = repository.getStory(prompt)) {
        is LoadResult.Success -> {
          story.value = result.data
          awaitAll(
            async { fetchAndStoreVoiceOver(0) },
            async { fetchAndStoreImage(0) })

          loadError.value = ""
          isLoading.value = false
        }

        is LoadResult.Error -> {
          loadError.value = result.message!!
          isLoading.value = false
        }
      }
    }
  }

  fun loadVoiceOver(paragraphIndex: Int) {
    voiceOverFile.value = null
    viewModelScope.launch(Dispatchers.IO) {
      if (story.value == null || paragraphIndex >= story.value!!.paragraphs.size) {
        return@launch
      }

      val currentParagraph = story.value!!.paragraphs[paragraphIndex]
      if (currentParagraph.voiceOver == null) {
        fetchAndStoreVoiceOver(paragraphIndex)
      }
      voiceOverFile.value = currentParagraph.voiceOver

      fetchAndStoreVoiceOver(paragraphIndex + 1)
    }
  }

  fun loadImage(paragraphIndex: Int) {
    imageUrl.value = null
    viewModelScope.launch {
      if (story.value == null || paragraphIndex >= story.value!!.paragraphs.size) {
        return@launch
      }

      val currentParagraph = story.value!!.paragraphs[paragraphIndex]
      if (currentParagraph.voiceOver == null) {
        imageIsLoading.value = true
        fetchAndStoreImage(paragraphIndex)
      }
      imageIsLoading.value = false
      imageUrl.value = currentParagraph.imageUrl

      fetchAndStoreImage(paragraphIndex + 1)
    }
  }

  private suspend fun fetchAndStoreVoiceOver(paragraphIndex: Int) {
    if (story.value == null || paragraphIndex >= story.value!!.paragraphs.size) {
      return
    }

    val currentParagraph = story.value!!.paragraphs[paragraphIndex]

    when (val result =
      repository.getVoiceOver(
        currentParagraph.content,
        "tempAudio_$paragraphIndex.mp3"
      )) {
      is LoadResult.Success -> {
        currentParagraph.voiceOver = result.data
      }

      is LoadResult.Error -> {
        Log.e(this.javaClass.simpleName, "Load voice over failed: ${result.message}")
      }
    }
  }

  private suspend fun fetchAndStoreImage(paragraphIndex: Int) {
    if (story.value == null || paragraphIndex >= story.value!!.paragraphs.size) {
      return
    }

    val currentParagraph = story.value!!.paragraphs[paragraphIndex]

    when (val result = repository.getImage(currentParagraph.content)) {
      is LoadResult.Success -> {
        currentParagraph.imageUrl = result.data ?: ""

        val request = ImageRequest.Builder(context)
          .data(currentParagraph.imageUrl)
          .build()
        context.imageLoader.enqueue(request).job.await()
      }

      is LoadResult.Error -> {
        Log.e(this.javaClass.simpleName, "Load image failed: ${result.message}")
      }
    }
  }
}