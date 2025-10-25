package me.okmanideep.droidpark.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import me.okmanideep.droidpark.data.DogApi
import me.okmanideep.droidpark.data.DogImage
import javax.inject.Inject
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    dogId: String,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("\uD83D\uDC36") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.errorMessage != null -> {
                    val error = state.errorMessage
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = error)
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(onClick = { viewModel.fetchDogDetail(dogId) }) {
                            Text(text = "Retry")
                        }
                    }
                }
                state.dog != null -> {
                    // Wrap image and breed info in a Column so the column contains the image as well
                    val dog = state.dog
                    val aspectRatio = dog.width.toFloat() / dog.height.toFloat()

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        AsyncImage(
                            model = dog.url,
                            contentDescription = "Dog image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(aspectRatio)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // If breeds available, show first breed name and group below image
                        if (dog.breeds.isNotEmpty()) {
                            val breed = dog.breeds.first()
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = breed.name, style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = breed.breedGroup, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                else -> {
                    // Fallback if nothing matches
                    Text(text = "No details available", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val dogApi: DogApi
): ViewModel() {
    private val _state = mutableStateOf(DetailState(isLoading = true))
    val state: State<DetailState> get() = _state

    init {
        val dogId: String = savedStateHandle["id"] ?: throw IllegalStateException("Missing dogId in DetailScreen")
        fetchDogDetail(dogId)
    }

    fun fetchDogDetail(dogId: String) {
        viewModelScope.launch {
            _state.value = DetailState(isLoading = true)
            try {
                val dog = dogApi.getImageById(dogId)
                _state.value = DetailState(dog = dog)
            } catch (e: Exception) {
                _state.value =
                    DetailState(errorMessage = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}

data class DetailState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val dog: DogImage? = null
)