package me.okmanideep.droidpark.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogImage(
    val id: String,
    val url: String,
    val width: Int = 640,
    val height: Int = 640,
    val breeds: List<DogBreed> = emptyList(),
)

@Serializable
data class DogBreed(
    val id: Int,
    val name: String,
    @SerialName("breed_group") val breedGroup: String = "",
)