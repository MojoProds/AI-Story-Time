package com.mojo.productions.aistorytime.ui.storypage

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.mojo.productions.aistorytime.data.model.Paragraph
import java.io.File

@Composable
fun StoryPageScreen(
  prompt: String,
  navController: NavController,
  viewModel: StoryPageViewModel = hiltViewModel(),
) {
  LaunchedEffect(prompt) {
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
    if (story != null && !isLoading) {
      val currentParagraph = remember { mutableStateOf(0) }
      val voiceOverFile by remember { viewModel.voiceOverFile }
      val imageUrl by remember { viewModel.imageUrl }
      val imageIsLoading by remember { viewModel.imageIsLoading }

      LaunchedEffect(currentParagraph.value) {
        viewModel.loadVoiceOver(currentParagraph.value)
        viewModel.loadImage(currentParagraph.value)
      }

      ParagraphDisplay(
        story!!.paragraphs[currentParagraph.value],
        imageUrl,
        imageIsLoading,
        voiceOverFile
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
  imageUrl: String?,
  imageIsLoading: Boolean,
  voiceOverFile: File?,
  onNextPageListener: () -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    SubcomposeAsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl?: "")
        .crossfade(true)
        .build(),
      contentDescription = paragraph.content,
      modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(20.dp))
        .shadow(elevation = 5.dp),
    ) {
      val state = painter.state
      if (state is AsyncImagePainter.State.Loading || imageIsLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
      } else {
        SubcomposeAsyncImageContent()
      }
    }
    Text(
      text = paragraph.content,
      modifier = Modifier.padding(20.dp),
      style = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
      ),
    )

    val context = LocalContext.current
    val voiceOverFinished = remember { mutableStateOf(false) }

    DisposableEffect(voiceOverFile) {
      val mediaPlayer = MediaPlayer()
      if (voiceOverFile != null) {
        mediaPlayer.setDataSource(context, voiceOverFile.toUri())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
          mediaPlayer.start()
        }
        mediaPlayer.setOnCompletionListener {
          voiceOverFinished.value = true
        }
      }
      onDispose() {
        mediaPlayer.stop()
        mediaPlayer.release()
      }
    }

    Button(
      onClick = {
        onNextPageListener.invoke()
        voiceOverFinished.value = false
      },
      modifier = Modifier.padding(20.dp),
      shape = CircleShape,
      enabled = voiceOverFinished.value
    ) {
      Icon(Icons.Filled.ArrowForward, "Next")
    }
  }
}