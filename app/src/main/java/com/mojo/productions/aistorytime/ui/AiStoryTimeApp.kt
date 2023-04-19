package com.mojo.productions.aistorytime.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mojo.productions.aistorytime.ui.theme.AIStoryTimeTheme

@Composable
fun AiStoryTimeApp() {
  AIStoryTimeTheme {
    StoryTimeNavHost()
  }
}

@Composable
fun StoryTimeNavHost(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  startDestination: String = "prompt",
) {
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = startDestination
  ) {
    composable("prompt") {
      PromptInput(onNavigateToStory = { navController.navigate("story") })
    }
    composable(
      "story/{content}",
      arguments = listOf(
        navArgument("content") {
          type = NavType.StringType
        }
      )
    ) {
      val content = remember {
        it.arguments?.getString("content")
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptInput(onNavigateToStory: () -> Unit, modifier: Modifier = Modifier) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = "Tell me a story about",
      modifier = modifier.padding(10.dp)
    )
    val promptText = remember {
      mutableStateOf(TextFieldValue())
    }
    TextField(
      value = promptText.value,
      onValueChange = { input -> promptText.value = input },
      modifier = modifier.padding(10.dp)
    )
    Button(
      onClick = onNavigateToStory,
      modifier = modifier.padding(10.dp)
    ) {
      Text(text = "Generate")
    }
  }
}