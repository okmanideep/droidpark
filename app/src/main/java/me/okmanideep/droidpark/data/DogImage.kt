package me.okmanideep.droidpark.data

import kotlinx.serialization.Serializable

@Serializable
data class DogImage(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)