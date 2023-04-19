package com.mojo.productions.aistorytime.data.story

import com.mojo.productions.aistorytime.model.Story

interface StoryDataService {
  suspend fun generateStory(prompt: String): Story
}