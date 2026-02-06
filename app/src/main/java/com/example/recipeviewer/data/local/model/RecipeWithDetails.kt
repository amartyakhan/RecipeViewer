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
        entity = RecipePartEntity::class,
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val parts: List<PartWithSteps>
)

data class PartWithSteps(
    @Embedded val part: RecipePartEntity,
    @Relation(
        entity = StepEntity::class,
        parentColumn = "id",
        entityColumn = "partId"
    )
    val steps: List<StepWithIngredients>
)
