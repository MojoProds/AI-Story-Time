package com.mojo.productions.aistorytime.data.remote.models

data class ChatGptRequest(
  val model: String,
  val messages: List<Message>,
)
