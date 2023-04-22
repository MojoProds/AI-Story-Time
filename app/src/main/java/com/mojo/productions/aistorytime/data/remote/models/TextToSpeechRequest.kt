package com.mojo.productions.aistorytime.data.remote.models

import com.google.gson.annotations.SerializedName

data class TextToSpeechRequest(
    val text: String,
    @SerializedName("voice_settings")
    val voiceSettings: VoiceSettings
)

data class VoiceSettings(
    val stability: Float,
    @SerializedName("similarity_boost")
    val similarityBoost: Float
)