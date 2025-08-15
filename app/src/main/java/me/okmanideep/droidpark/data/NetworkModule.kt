package me.okmanideep.droidpark.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import me.okmanideep.droidpark.BuildConfig
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DogApiClient

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    @DogApiClient
    fun provideDogApiClient(): HttpClient {
        return HttpClient(CIO) {
            install(DefaultRequest) {
                header("x-api-key", BuildConfig.THE_DOG_API_KEY)
            }

            install(ContentNegotiation) {
                json()
            }
        }
    }
}