package com.mojo.productions.aistorytime.data.remote.models

data class ImageResponse(
    val created: Int,
    val `data`: List<Data>
)

data class Data(
    val url: String
)