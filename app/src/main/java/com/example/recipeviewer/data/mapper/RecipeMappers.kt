package com.example.recipeviewer.data.mapper

import com.example.recipeviewer.data.local.model.IngredientEntity
import com.example.recipeviewer.data.local.model.RecipeEntity
import com.example.recipeviewer.data.local.model.RecipeWithDetails
import com.example.recipeviewer.data.local.model.StepEntity
import com.example.recipeviewer.data.local.model.StepWithIngredients
import com.example.recipeviewer.data.remote.model.IngredientDto
import com.example.recipeviewer.data.remote.model.RecipeDto
import com.example.recipeviewer.data.remote.model.StepDto
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
        steps = steps.sortedBy { it.step.order }.map { it.toDomain() }
    )
}

fun IngredientEntity.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        name = name,
        quantity = quantity,
        unit = unit
    )
}

fun StepWithIngredients.toDomain(): Step {
    return Step(
        id = step.id,
        order = step.order,
        instruction = step.instruction,
        stepIngredients = ingredients.map { it.toDomain() },
        durationMinutes = step.durationMinutes
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
        id = id,
        recipeId = recipeId,
        name = name,
        quantity = quantity,
        unit = unit
    )
}

fun Step.toEntity(recipeId: Long): StepEntity {
    return StepEntity(
        id = id,
        recipeId = recipeId,
        order = order,
        instruction = instruction,
        durationMinutes = durationMinutes
    )
}

fun RecipeDto.toDomain(): Recipe {
    return Recipe(
        title = title,
        imageUrl = imageUrl,
        prepTimeMinutes = prepTimeMinutes,
        cookTimeMinutes = cookTimeMinutes,
        servings = servings,
        ingredients = ingredients.map { it.toDomain() },
        steps = steps.map { it.toDomain() }
    )
}

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        name = name,
        quantity = quantity,
        unit = unit,
        id = 0
    )
}

fun StepDto.toDomain(): Step {
    return Step(
        order = order,
        instruction = instruction,
        durationMinutes = durationMinutes,
        stepIngredients = stepIngredients.map { it.toDomain() },
        id = 0
    )
}
