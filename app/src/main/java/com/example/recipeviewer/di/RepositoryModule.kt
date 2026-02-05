package com.example.recipeviewer.di

import com.example.recipeviewer.data.remote.JsoupRecipeScraper
import com.example.recipeviewer.data.remote.RecipeScraper
import com.example.recipeviewer.data.repository.RecipeRepositoryImpl
import com.example.recipeviewer.domain.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository

    @Binds
    @Singleton
    abstract fun bindRecipeScraper(
        jsoupRecipeScraper: JsoupRecipeScraper
    ): RecipeScraper
}
