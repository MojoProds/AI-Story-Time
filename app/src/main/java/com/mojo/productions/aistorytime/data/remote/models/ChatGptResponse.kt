package com.mojo.productions.aistorytime.data.remote.models

import com.google.gson.annotations.SerializedName

data class ChatGptResponse(
  val id: String,
  @SerializedName("object")
  val chatResponseObject: String,
  val created: Long,
  val model: String,
  val usage: Usage,
  val choices: List<Choice>,
)

data class Choice(
  val message: Message,
  @SerializedName("finish_reason")
  val finishReason: String,
  val index: Long,
)

data class Message(
  val role: String,
  val content: String,
)

data class Usage(
  @SerializedName("prompt_tokens")
  val promptTokens: Long,
  @SerializedName("completion_tokens")
  val completionTokens: Long,
  @SerializedName("total_tokens")
  val totalTokens: Long,
)