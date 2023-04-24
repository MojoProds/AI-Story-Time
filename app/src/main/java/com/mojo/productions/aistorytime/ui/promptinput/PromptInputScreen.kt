package com.mojo.productions.aistorytime.ui.promptinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PromptInputScreen(navController: NavController) {
  fun navigateToStory(prompt: String) {
    if (prompt.isNotEmpty()) {
      navController.navigate("story/${prompt}")
    }
  }

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
        modifier = Modifier.padding(10.dp),
        style = TextStyle(fontSize = 35.sp, fontWeight = FontWeight.Bold)
      )
      val promptText = remember { mutableStateOf(TextFieldValue()) }
      val focusRequester = remember { FocusRequester() }
      BasicTextField(
        value = promptText.value,
        onValueChange = { input -> promptText.value = input },
        modifier = Modifier
          .padding(10.dp)
          .focusRequester(focusRequester),
        textStyle = TextStyle(
          fontSize = 30.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.primary
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(onGo = { navigateToStory(promptText.value.text) })
      )
      LaunchedEffect(Unit) {
        focusRequester.requestFocus()
      }
    }
  }
}