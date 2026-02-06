package com.example.recipeviewer.domain.repository

import com.example.recipeviewer.data.local.dao.StepIngredientMapping
import com.example.recipeviewer.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getRecipeById(id: Long): Flow<Recipe?>
    suspend fun insertRecipe(
        recipe: Recipe,
        stepIngredientMappings: List<StepIngredientMapping> = emptyList()
    )
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun scrapeRecipeText(url: String): Result<String>
    suspend fun extractRecipe(scrapedText: String): Result<String>
    suspend fun extractAndSaveRecipe(scrapedText: String, url: String): Result<Unit>
    suspend fun toggleIngredientChecked(ingredientId: Long, isChecked: Boolean)
}
