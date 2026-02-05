package com.example.recipeviewer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipeviewer.ui.recipe_list.RecipeListScreen
import com.example.recipeviewer.ui.recipe_detail.RecipeDetailScreen
import com.example.recipeviewer.ui.cook_mode.CookModeScreen

sealed class Screen(val route: String) {
    object RecipeList : Screen("recipe_list")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: Long) = "recipe_detail/$recipeId"
    }
    object CookMode : Screen("cook_mode/{recipeId}/{multiplier}") {
        fun createRoute(recipeId: Long, multiplier: Float) = "cook_mode/$recipeId/$multiplier"
    }
}

@Composable
fun RecipeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.RecipeList.route
    ) {
        composable(route = Screen.RecipeList.route) {
            RecipeListScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                }
            )
        }
        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) {
            RecipeDetailScreen(
                onStartCooking = { recipeId, multiplier ->
                    navController.navigate(Screen.CookMode.createRoute(recipeId, multiplier))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.CookMode.route,
            arguments = listOf(
                navArgument("recipeId") { type = NavType.LongType },
                navArgument("multiplier") { type = NavType.FloatType }
            )
        ) {
            CookModeScreen(
                onClose = { navController.popBackStack() }
            )
        }
    }
}
