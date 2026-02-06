package com.example.recipeviewer.domain.model

data class Ingredient(
    val id: Long = 0,
    val name: String,
    val quantity: Double,
    val unit: String,
    val isChecked: Boolean = false
)
