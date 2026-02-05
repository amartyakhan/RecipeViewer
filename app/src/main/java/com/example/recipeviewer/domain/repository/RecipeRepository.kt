package com.example.recipeviewer.domain.repository

import com.example.recipeviewer.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getRecipeById(id: Long): Flow<Recipe?>
    suspend fun insertRecipe(
        recipe: Recipe,
        stepIngredientMappings: List<Pair<Int, Int>> = emptyList()
    )
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun scrapeRecipeText(url: String): Result<String>
}
