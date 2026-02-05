package com.example.recipeviewer.data.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StepWithIngredients(
    @Embedded val step: StepEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = StepIngredientEntity::class,
            parentColumn = "stepId",
            entityColumn = "ingredientId"
        )
    )
    val ingredients: List<IngredientEntity>
)
