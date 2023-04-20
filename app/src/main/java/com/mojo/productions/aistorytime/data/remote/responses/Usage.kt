package com.mojo.productions.aistorytime.data.remote.responses

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int,
)