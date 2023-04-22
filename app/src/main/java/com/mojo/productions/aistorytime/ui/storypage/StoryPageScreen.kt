package com.mojo.productions.aistorytime.ui.storypage

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mojo.productions.aistorytime.data.model.Story

@Composable
fun StoryPageScreen(
  prompt: String,
  viewModel: StoryPageViewModel = hiltViewModel(),
) {
  LaunchedEffect(Unit) {
    viewModel.loadStory(prompt)
  }

  val story by remember { viewModel.story }
  val loadError by remember { viewModel.loadError }
  val isLoading by remember { viewModel.isLoading }

  Surface(
    color = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxSize()
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      if (isLoading) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
      }
      if (loadError.isNotEmpty()) {
        Text(text = loadError)
      }
    }
    StoryDisplay(story, viewModel)
  }
}

@Composable
fun StoryDisplay(
  story: Story?,
  viewModel: StoryPageViewModel
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    if (story != null) {
      for (paragraph in story.paragraphs) {
        Text(
          text = paragraph.content,
          modifier = Modifier.padding(20.dp)
        )
      }

      LaunchedEffect(Unit) {
        viewModel.loadVoiceOver(story.paragraphs[0].content)
      }

      val voiceOverFile by remember { viewModel.voiceOverFile }

      val mediaPlayer = MediaPlayer()
      mediaPlayer.setAudioAttributes(
        AudioAttributes.Builder()
          .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
          .build()
      )

      if(voiceOverFile != null) {
        mediaPlayer.setDataSource(voiceOverFile)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
          mediaPlayer.start()
        }
      }
    }
  }
}