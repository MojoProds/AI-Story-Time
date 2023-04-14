package com.mojo.productions.aistorytime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mojo.productions.aistorytime.ui.theme.AIStoryTimeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AIStoryTimeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background) {
          PromptInput()
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptInput(modifier: Modifier = Modifier) {
  Column(horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center) {
    Text(
      text = "Tell me a story about",
      modifier = modifier.padding(10.dp)
    )
    val promptText = remember {
      mutableStateOf(TextFieldValue())
    }
    TextField(value = promptText.value,
              onValueChange = {input -> promptText.value = input},
              modifier = modifier.padding(10.dp))
    Button(onClick = { /*TODO*/ },
           modifier = modifier.padding(10.dp)) {
      Text(text = "Generate")
    }
  }
}