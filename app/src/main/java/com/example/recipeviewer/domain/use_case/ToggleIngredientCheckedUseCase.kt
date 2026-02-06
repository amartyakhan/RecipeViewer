package com.example.recipeviewer.domain.use_case

import com.example.recipeviewer.domain.repository.RecipeRepository
import javax.inject.Inject

class ToggleIngredientCheckedUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(ingredientId: Long, isChecked: Boolean) {
        repository.toggleIngredientChecked(ingredientId, isChecked)
    }
}
