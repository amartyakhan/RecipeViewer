package com.example.recipeviewer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val title: String,
    val imageUrl: String = "",
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val servings: Int = 1,
    val ingredients: List<IngredientDto>,
    val parts: List<RecipePartDto> = emptyList(),
    val steps: List<StepDto> = emptyList() // Fallback for old schema or if parts aren't used
)

@Serializable
data class RecipePartDto(
    val order: Int,
    val title: String? = null,
    val steps: List<StepDto>
)

@Serializable
data class IngredientDto(
    val name: String,
    val quantity: Double,
    val unit: String
)

@Serializable
data class StepDto(
    val order: Int,
    val instruction: String,
    val durationMinutes: Int = 0,
    val stepIngredients: List<IngredientDto> = emptyList()
)
