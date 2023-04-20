package com.mojo.productions.aistorytime.ui.storypage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mojo.productions.aistorytime.data.model.Story
import com.mojo.productions.aistorytime.data.repository.StoryRepository
import com.mojo.productions.aistorytime.util.LoadResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class StoryPageViewModel @Inject constructor(
  private val repository: StoryRepository,
) : ViewModel() {

  var story = mutableStateOf<Story?>(null)
  var loadError = mutableStateOf("")
  var isLoading = mutableStateOf(false)

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
}