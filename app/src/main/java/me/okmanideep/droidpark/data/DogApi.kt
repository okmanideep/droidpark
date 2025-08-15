package me.okmanideep.droidpark.data

import dagger.Reusable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

@Reusable
class DogApi(
    @param:DogApiClient private val client: HttpClient
) {
    suspend fun getRandomImages(): List<DogImage> {
        return client.get("https://api.thedogapi.com/v1/images/search?limit=10").body()
    }
}