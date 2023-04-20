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