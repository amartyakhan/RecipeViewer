package com.example.recipeviewer.data.repository

import com.example.recipeviewer.data.local.dao.RecipeDao
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
        stepIngredientMappings: List<Pair<Int, Int>>
    ) {
        withContext(Dispatchers.IO) {
            recipeDao.insertFullRecipe(
                recipe = recipe.toEntity(),
                ingredients = recipe.ingredients.map { it.toEntity(recipe.id) },
                steps = recipe.steps.map { it.toEntity(recipe.id) },
                stepIngredientMappings = stepIngredientMappings
            )
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
                
                // 3. Insert steps
                val stepEntities = recipe.steps.map { it.toEntity(recipeId) }
                val stepIds = recipeDao.insertSteps(stepEntities)
                
                // 4. Map step-specific ingredients to their IDs and insert relationships
                val stepIngredientEntities = mutableListOf<StepIngredientEntity>()
                recipe.steps.forEachIndexed { index, step ->
                    val stepId = stepIds[index]
                    step.stepIngredients.forEach { stepIngredient ->
                        // Find the corresponding ingredient ID from the main list by name
                        nameToIdMap[stepIngredient.name]?.let { ingredientId ->
                            stepIngredientEntities.add(
                                StepIngredientEntity(
                                    stepId = stepId,
                                    ingredientId = ingredientId
                                )
                            )
                        }
                    }
                }
                
                if (stepIngredientEntities.isNotEmpty()) {
                    recipeDao.insertStepIngredients(stepIngredientEntities)
                }
            }
        }
    }
}
