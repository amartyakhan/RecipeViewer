package com.example.recipeviewer.ui.recipe_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.use_case.DeleteRecipeUseCase
import com.example.recipeviewer.domain.use_case.GetRecipeByIdUseCase
import com.example.recipeviewer.domain.use_case.ScaleIngredientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val scaleIngredientsUseCase: ScaleIngredientsUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Long = checkNotNull(savedStateHandle["recipeId"])

    private val _multiplier = MutableStateFlow(1.0f)
    val multiplier: StateFlow<Float> = _multiplier.asStateFlow()

    private val _recipe = getRecipeByIdUseCase(recipeId)

    val uiState: StateFlow<RecipeDetailUiState> = combine(_recipe, _multiplier) { recipe, multiplier ->
        if (recipe == null) {
            RecipeDetailUiState.Error("Recipe not found")
        } else {
            val scaledRecipe = scaleIngredientsUseCase(recipe, multiplier)
            RecipeDetailUiState.Success(scaledRecipe)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecipeDetailUiState.Loading
    )

    fun setMultiplier(newMultiplier: Float) {
        _multiplier.value = newMultiplier
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            deleteRecipeUseCase(recipe)
        }
    }
}

sealed interface RecipeDetailUiState {
    object Loading : RecipeDetailUiState
    data class Success(val recipe: Recipe) : RecipeDetailUiState
    data class Error(val message: String) : RecipeDetailUiState
}
