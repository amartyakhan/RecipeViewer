package com.example.recipeviewer.data.remote

interface RecipeScraper {
    suspend fun scrapeText(url: String): Result<String>
    suspend fun extractImageUrl(url: String): String?
}
