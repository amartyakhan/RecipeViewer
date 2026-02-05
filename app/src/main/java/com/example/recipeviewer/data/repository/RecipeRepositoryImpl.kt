package com.example.recipeviewer.data.repository

import com.example.recipeviewer.data.local.dao.RecipeDao
import com.example.recipeviewer.data.mapper.toDomain
import com.example.recipeviewer.data.mapper.toEntity
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeRepository {

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
        recipeDao.insertFullRecipe(
            recipe = recipe.toEntity(),
            ingredients = recipe.ingredients.map { it.toEntity(recipe.id) },
            steps = recipe.steps.map { it.toEntity(recipe.id) },
            stepIngredientMappings = stepIngredientMappings
        )
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe.toEntity())
    }
}
