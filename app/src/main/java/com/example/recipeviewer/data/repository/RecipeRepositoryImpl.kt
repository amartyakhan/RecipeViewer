package com.example.recipeviewer.data.repository

import com.example.recipeviewer.data.local.dao.RecipeDao
import com.example.recipeviewer.data.local.dao.StepIngredientMapping
import com.example.recipeviewer.data.local.model.StepIngredientEntity
import com.example.recipeviewer.data.mapper.toDomain
import com.example.recipeviewer.data.mapper.toEntity
import com.example.recipeviewer.data.remote.RecipeExtractionDataSource
import com.example.recipeviewer.data.remote.RecipeScraper
import com.example.recipeviewer.data.remote.model.RecipeDto
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeScraper: RecipeScraper,
    private val recipeExtractionDataSource: RecipeExtractionDataSource
) : RecipeRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getRecipeById(id: Long): Flow<Recipe?> {
        return recipeDao.getRecipeById(id).map { it?.toDomain() }
    }

    override suspend fun insertRecipe(
        recipe: Recipe,
        stepIngredientMappings: List<StepIngredientMapping>
    ) {
        withContext(Dispatchers.IO) {
            // This is a bit complex to map from Domain back to the special insertFullRecipe structure
            // but for now, we can use the existing logic in extractAndSaveRecipe or implement a manual version here if needed.
            // For P0, most insertions come from extractAndSaveRecipe or PreloadData which bypasses this.
        }
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            recipeDao.deleteRecipe(recipe.toEntity())
        }
    }

    override suspend fun scrapeRecipeText(url: String): Result<String> {
        return recipeScraper.scrapeText(url)
    }

    override suspend fun extractRecipe(scrapedText: String): Result<String> {
        return recipeExtractionDataSource.extractRecipe(scrapedText)
    }

    override suspend fun extractAndSaveRecipe(scrapedText: String, url: String): Result<Unit> {
        return recipeExtractionDataSource.extractRecipe(scrapedText).mapCatching { jsonString ->
            val recipeDto = json.decodeFromString<RecipeDto>(jsonString)
            
            // Image Fallback Logic
            val finalImageUrl = if (recipeDto.imageUrl.isNullOrBlank()) {
                recipeScraper.extractImageUrl(url) ?: ""
            } else {
                recipeDto.imageUrl
            }
            
            val recipe = recipeDto.copy(imageUrl = finalImageUrl).toDomain()
            
            withContext(Dispatchers.IO) {
                // 1. Insert Recipe
                val recipeId = recipeDao.insertRecipe(recipe.toEntity())
                
                // 2. Insert all main ingredients and keep track of them by name
                val ingredientEntities = recipe.ingredients.map { it.toEntity(recipeId) }
                val ingredientIds = recipeDao.insertIngredients(ingredientEntities)
                val nameToIdMap = recipe.ingredients.zip(ingredientIds).associate { it.first.name to it.second }
                
                // 3. Insert parts and steps
                recipe.parts.forEach { part ->
                    val partId = recipeDao.insertRecipeParts(listOf(part.toEntity(recipeId))).first()
                    val stepEntities = part.steps.map { it.toEntity(recipeId, partId) }
                    val stepIds = recipeDao.insertSteps(stepEntities)
                    
                    // 4. Map step-specific ingredients
                    part.steps.forEachIndexed { stepIdx, step ->
                        val stepId = stepIds[stepIdx]
                        val stepIngredientEntities = step.stepIngredients.mapNotNull { stepIngredient ->
                            nameToIdMap[stepIngredient.name]?.let { ingredientId ->
                                StepIngredientEntity(
                                    stepId = stepId,
                                    ingredientId = ingredientId
                                )
                            }
                        }
                        if (stepIngredientEntities.isNotEmpty()) {
                            recipeDao.insertStepIngredients(stepIngredientEntities)
                        }
                    }
                }
            }
        }
    }
}
