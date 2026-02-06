package com.example.recipeviewer.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.recipeviewer.data.local.PreloadData
import com.example.recipeviewer.data.local.RecipeDatabase
import com.example.recipeviewer.data.local.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(
        @ApplicationContext context: Context,
        recipeDaoProvider: Provider<RecipeDao>
    ): RecipeDatabase {
        return Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "recipe_db"
        ).fallbackToDestructiveMigration()
        .addCallback(
            object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                        val dao = recipeDaoProvider.get()
                        try {
                            val count = dao.getRecipeCount()
                            Log.d("DatabaseModule", "Current recipe count: $count")
                            if (count == 0) {
                                Log.d("DatabaseModule", "Populating database with preloaded data...")
                                PreloadData.recipes.forEach { recipe ->
                                    val ingredients = PreloadData.ingredients.filter { it.recipeId == recipe.id }
                                    val parts = PreloadData.recipeParts[recipe.id] ?: emptyList()
                                    val mappings = PreloadData.stepIngredientMappings[recipe.id] ?: emptyList()
                                    dao.insertFullRecipe(recipe, ingredients, parts, mappings)
                                }
                                Log.d("DatabaseModule", "Database population complete.")
                            }
                        } catch (e: Exception) {
                            Log.e("DatabaseModule", "Error populating database", e)
                        }
                    }
                }
            }
        ).build()
    }

    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao
    }
}
