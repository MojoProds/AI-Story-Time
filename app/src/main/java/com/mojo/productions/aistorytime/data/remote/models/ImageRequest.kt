package com.mojo.productions.aistorytime.data.remote.models

enum class Size(val size: String) {
    SIZE_256("256x256"),
    SIZE_512("512x512"),
    SIZE_1024("1024x1024")
}

data class ImageRequest(
    val prompt: String,
    val n: Int,
    val size: String
)