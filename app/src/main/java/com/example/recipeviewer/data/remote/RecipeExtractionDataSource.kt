package com.example.recipeviewer.data.remote

import android.util.Log
import com.example.recipeviewer.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RecipeExtractionDataSource @Inject constructor() {

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            responseMimeType = "application/json"
        }
    )

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    suspend fun extractRecipe(scrapedText: String): Result<String> {
        return runCatching {
            val prompt = """
                You are a recipe extraction expert. Convert the following text into a structured JSON object representing a recipe.
                The JSON must follow this exact structure:
                {
                  "title": "Recipe Title",
                  "imageUrl": "Image URL if found, else empty string",
                  "prepTimeMinutes": 0,
                  "cookTimeMinutes": 0,
                  "servings": 1,
                  "ingredients": [
                    { "name": "Ingredient Name", "quantity": 1.0, "unit": "unit" }
                  ],
                  "steps": [
                    {
                      "order": 1,
                      "instruction": "Step instruction",
                      "durationMinutes": 0,
                      "stepIngredients": [
                        { "name": "Ingredient Name", "quantity": 1.0, "unit": "unit" }
                      ]
                    }
                  ]
                }
                
                Important: 
                - Ensure all ingredients used in a step are included in the 'stepIngredients' array for that step.
                - The quantities and units in 'stepIngredients' should match the main 'ingredients' list.
                - If prep or cook time is not specified, use 0.
                
                Text to parse:
                ${scrapedText}
            """.trimIndent()

            Log.d("RecipeExtraction", "Calling Gemini API with prompt length: ${prompt.length}")
            
            val response = withTimeout(120000) {
                model.generateContent(prompt)
            }

            val resultText = response.text ?: throw IllegalStateException("Gemini returned empty response")
            Log.d("RecipeExtraction", "Gemini response successful: ${resultText.take(200)}...")
            resultText
        }.onFailure { e ->
            Log.e("RecipeExtraction", "Error calling Gemini API: ${e.message}", e)
        }
    }
}
