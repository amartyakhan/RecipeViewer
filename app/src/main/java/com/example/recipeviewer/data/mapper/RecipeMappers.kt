package com.example.recipeviewer.data.mapper

import com.example.recipeviewer.data.local.model.IngredientEntity
import com.example.recipeviewer.data.local.model.RecipeEntity
import com.example.recipeviewer.data.local.model.RecipeWithDetails
import com.example.recipeviewer.data.local.model.StepEntity
import com.example.recipeviewer.domain.model.Ingredient
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.model.Step

fun RecipeWithDetails.toDomain(): Recipe {
    return Recipe(
        id = recipe.id,
        title = recipe.title,
        imageUrl = recipe.imageUrl,
        prepTimeMinutes = recipe.prepTimeMinutes,
        cookTimeMinutes = recipe.cookTimeMinutes,
        servings = recipe.servings,
        ingredients = ingredients.map { it.toDomain() },
        steps = steps.sortedBy { it.order }.map { it.toDomain(ingredients) }
    )
}

fun IngredientEntity.toDomain(): Ingredient {
    return Ingredient(
        name = name,
        quantity = quantity,
        unit = unit
    )
}

fun StepEntity.toDomain(allIngredients: List<IngredientEntity>): Step {
    // In a real app, you might have a many-to-many relationship between steps and ingredients.
    // For this P0, we'll assume the domain model's stepIngredients can be derived or simplified.
    // If the design doc implies specific ingredients per step, the schema might need adjusting,
    // but for now, we'll keep it simple as per the current schema.
    return Step(
        order = order,
        instruction = instruction,
        stepIngredients = emptyList(), // Simplified for now as per schema
        durationMinutes = durationMinutes
    )
}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        prepTimeMinutes = prepTimeMinutes,
        cookTimeMinutes = cookTimeMinutes,
        servings = servings
    )
}

fun Ingredient.toEntity(recipeId: Long): IngredientEntity {
    return IngredientEntity(
        recipeId = recipeId,
        name = name,
        quantity = quantity,
        unit = unit
    )
}

fun Step.toEntity(recipeId: Long): StepEntity {
    return StepEntity(
        recipeId = recipeId,
        order = order,
        instruction = instruction,
        durationMinutes = durationMinutes
    )
}
