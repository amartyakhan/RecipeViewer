package com.example.recipeviewer.domain.model

data class Recipe(
    val id: Long = 0,
    val title: String,
    val imageUrl: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val ingredients: List<Ingredient>,
    val parts: List<RecipePart>,
    val servings: Int = 1
)
