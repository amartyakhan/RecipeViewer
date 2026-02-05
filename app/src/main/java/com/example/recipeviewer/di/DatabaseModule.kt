package com.example.recipeviewer.di

import android.content.Context
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
        ).addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                        val dao = recipeDaoProvider.get()
                        PreloadData.recipes.forEach { recipe ->
                            val ingredients = PreloadData.ingredients.filter { it.recipeId == recipe.id }
                            val steps = PreloadData.steps.filter { it.recipeId == recipe.id }
                            dao.insertFullRecipe(recipe, ingredients, steps)
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
