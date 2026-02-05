package com.example.recipeviewer.data.local

import com.example.recipeviewer.data.local.model.IngredientEntity
import com.example.recipeviewer.data.local.model.RecipeEntity
import com.example.recipeviewer.data.local.model.StepEntity

object PreloadData {
    val recipes = listOf(
        RecipeEntity(
            id = 1,
            title = "Fluffy Pancakes",
            imageUrl = "https://wareaglemill.com/wp-content/uploads/2023/07/Apple-butter-pancakes-300x300.jpeg",
            prepTimeMinutes = 10,
            cookTimeMinutes = 15,
            servings = 4
        ),
        RecipeEntity(
            id = 2,
            title = "Spaghetti Bolognese",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcL2SPd2nGLPbSX9QmmTVuP-WlXeMUApZ2TA&s",
            prepTimeMinutes = 15,
            cookTimeMinutes = 45,
            servings = 4
        ),
        RecipeEntity(
            id = 3,
            title = "Chocolate Chip Cookies",
            imageUrl = "https://joyfoodsunshine.com/wp-content/uploads/2018/02/best-chocolate-chip-cookies-recipe-1.jpg",
            prepTimeMinutes = 20,
            cookTimeMinutes = 10,
            servings = 24
        )
    )

    val ingredients = listOf(
        // Pancakes (RecipeId 1)
        IngredientEntity(id = 1, recipeId = 1, name = "All-purpose flour", quantity = 1.5, unit = "cups"), // 0
        IngredientEntity(id = 2, recipeId = 1, name = "Baking powder", quantity = 3.5, unit = "tsp"), // 1
        IngredientEntity(id = 3, recipeId = 1, name = "Salt", quantity = 1.0, unit = "tsp"), // 2
        IngredientEntity(id = 4, recipeId = 1, name = "White sugar", quantity = 1.0, unit = "tbsp"), // 3
        IngredientEntity(id = 5, recipeId = 1, name = "Milk", quantity = 1.25, unit = "cups"), // 4
        IngredientEntity(id = 6, recipeId = 1, name = "Egg", quantity = 1.0, unit = "large"), // 5
        IngredientEntity(id = 7, recipeId = 1, name = "Butter, melted", quantity = 3.0, unit = "tbsp"), // 6

        // Spaghetti Bolognese (RecipeId 2)
        IngredientEntity(id = 8, recipeId = 2, name = "Spaghetti", quantity = 400.0, unit = "g"), // 0
        IngredientEntity(id = 9, recipeId = 2, name = "Ground beef", quantity = 500.0, unit = "g"), // 1
        IngredientEntity(id = 10, recipeId = 2, name = "Onion, chopped", quantity = 1.0, unit = "medium"), // 2
        IngredientEntity(id = 11, recipeId = 2, name = "Garlic cloves, minced", quantity = 2.0, unit = "cloves"), // 3
        IngredientEntity(id = 12, recipeId = 2, name = "Canned tomatoes", quantity = 800.0, unit = "g"), // 4
        IngredientEntity(id = 13, recipeId = 2, name = "Tomato paste", quantity = 2.0, unit = "tbsp"), // 5
        IngredientEntity(id = 14, recipeId = 2, name = "Dried oregano", quantity = 1.0, unit = "tsp"), // 6

        // Chocolate Chip Cookies (RecipeId 3)
        IngredientEntity(id = 15, recipeId = 3, name = "Butter, softened", quantity = 1.0, unit = "cup"), // 0
        IngredientEntity(id = 16, recipeId = 3, name = "White sugar", quantity = 1.0, unit = "cup"), // 1
        IngredientEntity(id = 17, recipeId = 3, name = "Brown sugar", quantity = 1.0, unit = "cup"), // 2
        IngredientEntity(id = 18, recipeId = 3, name = "Eggs", quantity = 2.0, unit = "large"), // 3
        IngredientEntity(id = 19, recipeId = 3, name = "Vanilla extract", quantity = 2.0, unit = "tsp"), // 4
        IngredientEntity(id = 20, recipeId = 3, name = "Baking soda", quantity = 1.0, unit = "tsp"), // 5
        IngredientEntity(id = 21, recipeId = 3, name = "Hot water", quantity = 2.0, unit = "tsp"), // 6
        IngredientEntity(id = 22, recipeId = 3, name = "Salt", quantity = 0.5, unit = "tsp"), // 7
        IngredientEntity(id = 23, recipeId = 3, name = "All-purpose flour", quantity = 3.0, unit = "cups"), // 8
        IngredientEntity(id = 24, recipeId = 3, name = "Semisweet chocolate chips", quantity = 2.0, unit = "cups") // 9
    )

    val steps = listOf(
        // Pancakes
        StepEntity(id = 1, recipeId = 1, order = 1, instruction = "Sift flour, baking powder, salt and sugar together in a large bowl.", durationMinutes = 5),
        StepEntity(id = 2, recipeId = 1, order = 2, instruction = "Make a well in the center and pour in the milk, egg and melted butter; mix until smooth.", durationMinutes = 5),
        StepEntity(id = 3, recipeId = 1, order = 3, instruction = "Heat a lightly oiled griddle or frying pan over medium-high heat.", durationMinutes = 2),
        StepEntity(id = 4, recipeId = 1, order = 4, instruction = "Pour or scoop the batter onto the griddle, using approximately 1/4 cup for each pancake. Brown on both sides and serve hot.", durationMinutes = 10),

        // Spaghetti Bolognese
        StepEntity(id = 5, recipeId = 2, order = 1, instruction = "Heat oil in a large saucepan. Add onion and garlic, and cook until softened.", durationMinutes = 5),
        StepEntity(id = 6, recipeId = 2, order = 2, instruction = "Add ground beef and cook until browned.", durationMinutes = 10),
        StepEntity(id = 7, recipeId = 2, order = 3, instruction = "Stir in tomatoes, tomato paste, and oregano. Simmer for 30 minutes.", durationMinutes = 30),
        StepEntity(id = 8, recipeId = 2, order = 4, instruction = "Cook spaghetti in a large pot of boiling salted water until al dente. Drain and serve with bolognese sauce.", durationMinutes = 10),

        // Chocolate Chip Cookies
        StepEntity(id = 9, recipeId = 3, order = 1, instruction = "Preheat oven to 350 degrees F (175 degrees C).", durationMinutes = 10),
        StepEntity(id = 10, recipeId = 3, order = 2, instruction = "Cream together the butter, white sugar, and brown sugar until smooth. Beat in the eggs one at a time, then stir in the vanilla.", durationMinutes = 10),
        StepEntity(id = 11, recipeId = 3, order = 3, instruction = "Dissolve baking soda in hot water. Add to batter along with salt.", durationMinutes = 2),
        StepEntity(id = 12, recipeId = 3, order = 4, instruction = "Stir in flour and chocolate chips.", durationMinutes = 5),
        StepEntity(id = 13, recipeId = 3, order = 5, instruction = "Drop by large spoonfuls onto ungreased pans. Bake for about 10 minutes, or until edges are nicely browned.", durationMinutes = 10)
    )

    // (stepIndex, ingredientIndex) relative to the recipe's own list
    val stepIngredientMappings = mapOf(
        1L to listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 1 to 4, 1 to 5, 1 to 6),
        2L to listOf(0 to 2, 0 to 3, 1 to 1, 2 to 4, 2 to 5, 2 to 6, 3 to 0),
        3L to listOf(1 to 0, 1 to 1, 1 to 2, 1 to 3, 1 to 4, 2 to 5, 2 to 6, 2 to 7, 3 to 8, 3 to 9)
    )
}
