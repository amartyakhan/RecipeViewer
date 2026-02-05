package com.example.recipeviewer.ui.cook_mode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.use_case.GetRecipeByIdUseCase
import com.example.recipeviewer.domain.use_case.ScaleIngredientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CookModeViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val scaleIngredientsUseCase: ScaleIngredientsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Long = checkNotNull(savedStateHandle["recipeId"])
    private val multiplier: Float = savedStateHandle.get<Float>("multiplier") ?: 1.0f

    private val _recipe = getRecipeByIdUseCase(recipeId)

    val uiState: StateFlow<CookModeUiState> = _recipe.map { recipe ->
        if (recipe == null) {
            CookModeUiState.Error("Recipe not found")
        } else {
            val scaledRecipe = scaleIngredientsUseCase(recipe, multiplier)
            CookModeUiState.Success(scaledRecipe)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CookModeUiState.Loading
    )
}

sealed interface CookModeUiState {
    object Loading : CookModeUiState
    data class Success(val recipe: Recipe) : CookModeUiState
    data class Error(val message: String) : CookModeUiState
}
