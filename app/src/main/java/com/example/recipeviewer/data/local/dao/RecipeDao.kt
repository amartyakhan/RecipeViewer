package com.example.recipeviewer.data.local.dao

import androidx.room.*
import com.example.recipeviewer.data.local.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Transaction
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeWithDetails>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Long): Flow<RecipeWithDetails?>

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun getRecipeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeParts(parts: List<RecipePartEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<StepEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStepIngredients(stepIngredients: List<StepIngredientEntity>)

    @Transaction
    suspend fun insertFullRecipe(
        recipe: RecipeEntity,
        ingredients: List<IngredientEntity>,
        parts: List<RecipePartWithStepsEntities>,
        stepIngredientMappings: List<StepIngredientMapping> = emptyList()
    ) {
        val recipeId = insertRecipe(recipe)
        val ingredientIds = insertIngredients(ingredients.map { it.copy(recipeId = recipeId) })

        parts.forEachIndexed { partIdx, partWithSteps ->
            val partId = insertRecipeParts(listOf(partWithSteps.part.copy(recipeId = recipeId))).first()
            val stepIds = insertSteps(partWithSteps.steps.map { it.copy(recipeId = recipeId, partId = partId) })

            val mappings = stepIngredientMappings
                .filter { it.partIndex == partIdx }
                .map { mapping ->
                    StepIngredientEntity(
                        stepId = stepIds[mapping.stepIndex],
                        ingredientId = ingredientIds[mapping.ingredientIndex]
                    )
                }
            insertStepIngredients(mappings)
        }
    }

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}

data class RecipePartWithStepsEntities(
    val part: RecipePartEntity,
    val steps: List<StepEntity>
)

data class StepIngredientMapping(
    val partIndex: Int,
    val stepIndex: Int,
    val ingredientIndex: Int
)
