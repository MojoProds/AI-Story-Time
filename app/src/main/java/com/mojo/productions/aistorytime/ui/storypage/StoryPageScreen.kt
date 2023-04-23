package com.mojo.productions.aistorytime.ui.storypage

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mojo.productions.aistorytime.data.model.Paragraph

@Composable
fun StoryPageScreen(
  prompt: String,
  navController: NavController,
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
    if (story != null) {
      val currentParagraph = remember { mutableStateOf(0) }
      ParagraphDisplay(
        story!!.paragraphs[currentParagraph.value],
        viewModel
      ) {
        if (currentParagraph.value < story!!.paragraphs.size - 1) {
          currentParagraph.value++
        } else {
          navController.navigate("prompt") {
            popUpTo("prompt") { inclusive = true }
          }
        }
      }
    }
  }
}

@Composable
fun ParagraphDisplay(
  paragraph: Paragraph,
  viewModel: StoryPageViewModel,
  onNextPageListener: () -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = paragraph.content,
      modifier = Modifier.padding(20.dp)
    )

    LaunchedEffect(paragraph.content) {
      viewModel.loadVoiceOver(paragraph.content)
    }

    val context = LocalContext.current
    val voiceOverFile by remember { viewModel.voiceOverFile }
    val voiceOverFinished = remember { mutableStateOf(false) }

    DisposableEffect(voiceOverFile) {
      val mediaPlayer = MediaPlayer()
      if (voiceOverFile != null) {
        mediaPlayer.setDataSource(context, voiceOverFile!!.toUri())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
          mediaPlayer.start()
        }
        mediaPlayer.setOnCompletionListener {
          viewModel.clearVoiceOver()
          voiceOverFinished.value = true
        }
      }
      onDispose() {
        mediaPlayer.stop()
        mediaPlayer.release()
      }
    }

    if (voiceOverFinished.value) {
      FloatingActionButton(onClick = {
        onNextPageListener.invoke()
        voiceOverFinished.value = false
      }) {
        Text(
          text = "Next"
        )
      }
    }
  }
}