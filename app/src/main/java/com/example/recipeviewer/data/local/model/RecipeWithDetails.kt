package com.example.recipeviewer.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithDetails(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<IngredientEntity>,
    @Relation(
        entity = StepEntity::class,
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val steps: List<StepWithIngredients>
)
