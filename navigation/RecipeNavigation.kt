package com.example.recipeviewer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipeviewer.ui.recipe_list.RecipeListScreen
import com.example.recipeviewer.ui.recipe_detail.RecipeDetailScreen

sealed class Screen(val route: String) {
    object RecipeList : Screen("recipe_list")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: Long) = "recipe_detail/$recipeId"
    }
    object CookMode : Screen("cook_mode/{recipeId}") {
        fun createRoute(recipeId: Long) = "cook_mode/$recipeId"
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
                onStartCooking = { recipeId -> 
                    navController.navigate(Screen.CookMode.createRoute(recipeId)) 
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.CookMode.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
        ) {
            // TODO: CookModeScreen(onClose = { navController.popBackStack() })
        }
    }
}
