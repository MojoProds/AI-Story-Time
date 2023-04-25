package com.mojo.productions.aistorytime.data.model

import java.io.File

data class Story(
  val paragraphs: List<Paragraph> = emptyList(),
)

data class Paragraph(
  val content: String,
  var imageUrl: String = "",
  var voiceOver: File? = null,
)