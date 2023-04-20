package com.mojo.productions.aistorytime.ui.promptinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptInputScreen(onNavigateToStory: () -> Unit, modifier: Modifier = Modifier) {
  Surface(
    color = MaterialTheme.colorScheme.background,
    modifier = Modifier.fillMaxSize()
  ) {
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
        modifier = modifier.padding(10.dp),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(onGo = { onNavigateToStory.invoke() })
      )
      Button(
        onClick = onNavigateToStory,
        modifier = modifier.padding(10.dp)
      ) {
        Text(text = "Generate")
      }
    }
  }
}