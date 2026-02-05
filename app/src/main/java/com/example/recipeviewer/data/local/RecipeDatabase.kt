package com.example.recipeviewer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeviewer.data.local.dao.RecipeDao
import com.example.recipeviewer.data.local.model.IngredientEntity
import com.example.recipeviewer.data.local.model.RecipeEntity
import com.example.recipeviewer.data.local.model.StepEntity

@Database(
    entities = [RecipeEntity::class, IngredientEntity::class, StepEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}
