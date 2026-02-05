package com.example.recipeviewer.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "step_ingredients",
    primaryKeys = ["stepId", "ingredientId"],
    foreignKeys = [
        ForeignKey(
            entity = StepEntity::class,
            parentColumns = ["id"],
            childColumns = ["stepId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = ["id"],
            childColumns = ["ingredientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("stepId"), Index("ingredientId")]
)
data class StepIngredientEntity(
    val stepId: Long,
    val ingredientId: Long
)
