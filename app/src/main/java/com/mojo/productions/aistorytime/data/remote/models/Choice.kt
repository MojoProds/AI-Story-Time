package com.mojo.productions.aistorytime.data.remote.models

import com.google.gson.annotations.SerializedName

data class Choice(
  val message: Message,
  @SerializedName("finish_reason")
  val finishReason: String,
  val index: Long,
)