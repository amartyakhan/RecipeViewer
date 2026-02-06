package com.example.recipeviewer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeviewer.data.local.dao.RecipeDao
import com.example.recipeviewer.data.local.model.*

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        RecipePartEntity::class,
        StepEntity::class,
        StepIngredientEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}
