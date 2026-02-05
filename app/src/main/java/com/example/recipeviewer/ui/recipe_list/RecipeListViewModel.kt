package com.example.recipeviewer.ui.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.repository.RecipeRepository
import com.example.recipeviewer.domain.use_case.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    getRecipesUseCase: GetRecipesUseCase,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _scrapeResult = MutableSharedFlow<Result<String>>()
    val scrapeResult = _scrapeResult.asSharedFlow()

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
            val result = recipeRepository.scrapeRecipeText(url)
            _scrapeResult.emit(result)
        }
    }
}

sealed interface RecipeListUiState {
    object Loading : RecipeListUiState
    data class Success(val recipes: List<Recipe>) : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}
