package com.example.recipeviewer.ui.cook_mode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.use_case.GetRecipeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CookModeViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Long = checkNotNull(savedStateHandle["recipeId"])

    private val _recipe = getRecipeByIdUseCase(recipeId)

    val uiState: StateFlow<CookModeUiState> = _recipe.map { recipe ->
        if (recipe == null) {
            CookModeUiState.Error("Recipe not found")
        } else {
            CookModeUiState.Success(recipe)
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
