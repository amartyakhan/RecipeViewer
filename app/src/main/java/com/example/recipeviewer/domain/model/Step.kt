package com.example.recipeviewer.domain.model

data class Step(
    val id: Long = 0,
    val order: Int,
    val instruction: String,
    val stepIngredients: List<Ingredient>,
    val durationMinutes: Int? = null
)
