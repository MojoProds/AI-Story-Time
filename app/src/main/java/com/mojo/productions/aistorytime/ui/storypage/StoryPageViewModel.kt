package com.mojo.productions.aistorytime.ui.storypage

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mojo.productions.aistorytime.data.model.Story
import com.mojo.productions.aistorytime.data.repository.StoryRepository
import com.mojo.productions.aistorytime.util.LoadResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class StoryPageViewModel @Inject constructor(
  private val repository: StoryRepository,
) : ViewModel() {

  var story = mutableStateOf<Story?>(null)
  var loadError = mutableStateOf("")
  var isLoading = mutableStateOf(false)

  var voiceOverFile = mutableStateOf<File?>(null)

  fun loadStory(prompt: String) {
    viewModelScope.launch {
      isLoading.value = true

      val result = repository.getStory(prompt)
      when (result) {
        is LoadResult.Success -> {
          story.value = result.data

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

  fun loadVoiceOver(content: String) {
    viewModelScope.launch(Dispatchers.IO) {
      when (val result = repository.getVoiceOver(content)) {
        is LoadResult.Success -> {
          voiceOverFile.value = result.data
        }

        is LoadResult.Error -> {
          Log.e(this.javaClass.simpleName, "Load voice over failed: ${result.message}")
        }
      }
    }
  }

  fun clearVoiceOver() {
    voiceOverFile.value = null
  }
}