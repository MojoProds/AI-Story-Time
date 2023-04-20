package com.mojo.productions.aistorytime.data.remote.responses

data class Choice(
  val finish_reason: String,
  val index: Int,
  val message: Message,
)