package com.example.recipeviewer.domain.model

data class RecipePart(
    val id: Long = 0,
    val order: Int,
    val title: String? = null,
    val steps: List<Step>
)
