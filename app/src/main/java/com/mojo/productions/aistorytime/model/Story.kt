package com.mojo.productions.aistorytime.model

data class Story(
  val paragraphs: List<Paragraph> = emptyList(),
)

data class Paragraph(
  val content: String,
  val imageUrl: String,
  val speechUrl: String,
)