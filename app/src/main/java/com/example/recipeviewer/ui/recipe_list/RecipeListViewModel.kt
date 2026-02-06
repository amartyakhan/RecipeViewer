package com.example.recipeviewer.ui.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.repository.RecipeRepository
import com.example.recipeviewer.domain.use_case.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _extractionUiState = MutableStateFlow<ExtractionUiState>(ExtractionUiState.Idle)
    val extractionUiState = _extractionUiState.asStateFlow()

    val uiState: StateFlow<RecipeListUiState> = getRecipesUseCase()
        .map { recipes ->
            RecipeListUiState.Success(recipes)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecipeListUiState.Loading
        )

    fun onGetRecipe(url: String) {
        viewModelScope.launch {
            _extractionUiState.value = ExtractionUiState.Loading
            val scrapeResult = recipeRepository.scrapeRecipeText(url)
            scrapeResult.onSuccess { text ->
                val saveResult = recipeRepository.extractAndSaveRecipe(text, url)
                saveResult.onSuccess {
                    _extractionUiState.value = ExtractionUiState.Success("Recipe added successfully")
                }.onFailure { error ->
                    _extractionUiState.value = ExtractionUiState.Error(getErrorMessage(error))
                }
            }.onFailure { error ->
                _extractionUiState.value = ExtractionUiState.Error(getErrorMessage(error))
            }
        }
    }

    fun dismissExtraction() {
        _extractionUiState.value = ExtractionUiState.Idle
    }

    private fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is TimeoutCancellationException -> "Request timed out. Please try again."
            else -> {
                val errorMessage = error.message ?: ""
                when {
                    errorMessage.contains("503") -> "Service not available. Please try again later."
                    errorMessage.contains("429") -> "Too many requests. Please wait a moment."
                    else -> error.message ?: "An unexpected error occurred."
                }
            }
        }
    }
}

sealed interface RecipeListUiState {
    object Loading : RecipeListUiState
    data class Success(val recipes: List<Recipe>) : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}

sealed interface ExtractionUiState {
    object Idle : ExtractionUiState
    object Loading : ExtractionUiState
    data class Success(val recipeName: String) : ExtractionUiState
    data class Error(val message: String) : ExtractionUiState
}
