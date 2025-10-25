package me.okmanideep.droidpark.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.okmanideep.droidpark.data.DogApi
import me.okmanideep.droidpark.data.DogImage
import me.okmanideep.droidpark.ui.theme.OffWhite
import javax.inject.Inject
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) { Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dogs \uD83D\uDC36") })
        }
    ) { innerPadding ->
        val state by viewModel.state

        when {
            state.isLoading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.errorMessage != null -> {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Error: ${state.errorMessage}")
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.fetchDogImages() }) {
                        Text(text = "Retry")
                    }
                }
            }
            else -> {
                val dogImages = state.dogImages

                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    contentPadding = innerPadding
                ) {
                    items(dogImages, key = { "dog-${it.id}" }) { dogImage ->
                        val aspectRatio = dogImage.width.toFloat() / dogImage.height.toFloat()

                        AsyncImage(
                            model = dogImage.url,
                            contentDescription = "Dog image ${dogImage.id}",
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(OffWhite)
                                .aspectRatio(aspectRatio)
                                .clickable { navController.navigateToDetails(dogImage.id) }
                            ,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}

// Home screen UI state
data class HomeState(
    val dogImages: List<DogImage> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dogApi: DogApi
) : ViewModel() {
    private val _state: MutableState<HomeState> = mutableStateOf(HomeState())
    val state: State<HomeState> get() = _state

    init {
        fetchDogImages()
    }

    fun fetchDogImages() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val images = dogApi.getRandomImages()
                _state.value = _state.value.copy(dogImages = images, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(dogImages = listOf(), isLoading = false, errorMessage = e.message ?: "Unknown error")
            }
        }
    }
}