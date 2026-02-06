package com.example.recipeviewer.domain.use_case

import com.example.recipeviewer.domain.model.Recipe
import javax.inject.Inject

class ScaleIngredientsUseCase @Inject constructor() {
    operator fun invoke(recipe: Recipe, multiplier: Float): Recipe {
        return recipe.copy(
            ingredients = recipe.ingredients.map { ingredient ->
                ingredient.copy(quantity = ingredient.quantity * multiplier)
            },
            parts = recipe.parts.map { part ->
                part.copy(
                    steps = part.steps.map { step ->
                        step.copy(
                            stepIngredients = step.stepIngredients.map { ingredient ->
                                ingredient.copy(quantity = ingredient.quantity * multiplier)
                            }
                        )
                    }
                )
            }
        )
    }
}
