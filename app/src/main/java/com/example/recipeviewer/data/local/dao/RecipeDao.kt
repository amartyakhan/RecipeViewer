package com.example.recipeviewer.data.local.dao

import androidx.room.*
import com.example.recipeviewer.data.local.model.IngredientEntity
import com.example.recipeviewer.data.local.model.RecipeEntity
import com.example.recipeviewer.data.local.model.RecipeWithDetails
import com.example.recipeviewer.data.local.model.StepEntity
import com.example.recipeviewer.data.local.model.StepIngredientEntity
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
    suspend fun insertSteps(steps: List<StepEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStepIngredients(stepIngredients: List<StepIngredientEntity>)

    @Transaction
    suspend fun insertFullRecipe(
        recipe: RecipeEntity,
        ingredients: List<IngredientEntity>,
        steps: List<StepEntity>,
        stepIngredientMappings: List<Pair<Int, Int>> = emptyList() // (stepIndex, ingredientIndex)
    ) {
        val recipeId = insertRecipe(recipe)
        val ingredientIds = insertIngredients(ingredients.map { it.copy(recipeId = recipeId) })
        val stepIds = insertSteps(steps.map { it.copy(recipeId = recipeId) })

        val mappings = stepIngredientMappings.map { (stepIdx, ingIdx) ->
            StepIngredientEntity(
                stepId = stepIds[stepIdx],
                ingredientId = ingredientIds[ingIdx]
            )
        }
        insertStepIngredients(mappings)
    }

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}
