package com.mojo.productions.aistorytime.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mojo.productions.aistorytime.ui.promptinput.PromptInputScreen
import com.mojo.productions.aistorytime.ui.storypage.StoryPageScreen
import com.mojo.productions.aistorytime.ui.theme.AIStoryTimeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AIStoryTimeTheme {
        val navController = rememberNavController()
        NavHost(
          navController = navController,
          startDestination = "prompt"
        ) {
          composable("prompt") {
            PromptInputScreen(navController)
          }
          composable(
            "story/{prompt}",
            arguments = listOf(
              navArgument("prompt") {
                type = NavType.StringType
              }
            )
          ) {
            val prompt = remember {
              it.arguments?.getString("prompt")
            }
            StoryPageScreen(prompt!!, navController)
          }
        }
      }
    }
  }
}