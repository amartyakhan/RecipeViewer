package com.example.recipeviewer.data.mapper

import com.example.recipeviewer.data.local.model.*
import com.example.recipeviewer.data.remote.model.IngredientDto
import com.example.recipeviewer.data.remote.model.RecipeDto
import com.example.recipeviewer.data.remote.model.RecipePartDto
import com.example.recipeviewer.data.remote.model.StepDto
import com.example.recipeviewer.domain.model.Ingredient
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.model.RecipePart
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
        parts = parts.sortedBy { it.part.order }.map { it.toDomain() }
    )
}

fun PartWithSteps.toDomain(): RecipePart {
    return RecipePart(
        id = part.id,
        order = part.order,
        title = part.title,
        steps = steps.sortedBy { it.step.order }.map { it.toDomain() }
    )
}

fun IngredientEntity.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        name = name,
        quantity = quantity,
        unit = unit,
        isChecked = isChecked
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

fun RecipePart.toEntity(recipeId: Long): RecipePartEntity {
    return RecipePartEntity(
        id = id,
        recipeId = recipeId,
        order = order,
        title = title
    )
}

fun Ingredient.toEntity(recipeId: Long): IngredientEntity {
    return IngredientEntity(
        id = id,
        recipeId = recipeId,
        name = name,
        quantity = quantity,
        unit = unit,
        isChecked = isChecked
    )
}

fun Step.toEntity(recipeId: Long, partId: Long): StepEntity {
    return StepEntity(
        id = id,
        recipeId = recipeId,
        partId = partId,
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
        parts = parts.map { it.toDomain() }
    )
}

fun RecipePartDto.toDomain(): RecipePart {
    return RecipePart(
        order = order,
        title = title,
        steps = steps.map { it.toDomain() }
    )
}

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        name = name,
        quantity = quantity,
        unit = unit,
        id = 0,
        isChecked = false
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
