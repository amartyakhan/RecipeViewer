package com.example.recipeviewer.ui.recipe_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.recipeviewer.domain.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    onStartCooking: (Long, Float) -> Unit,
    onBack: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val multiplier by viewModel.multiplier.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is RecipeDetailUiState.Success) {
                        Text((uiState as RecipeDetailUiState.Success).recipe.title)
                    } else {
                        Text("Recipe Detail")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is RecipeDetailUiState.Success) {
                        IconButton(onClick = {
                            viewModel.deleteRecipe((uiState as RecipeDetailUiState.Success).recipe)
                            onBack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Recipe")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is RecipeDetailUiState.Success) {
                ExtendedFloatingActionButton(
                    onClick = { 
                        val recipe = (uiState as RecipeDetailUiState.Success).recipe
                        onStartCooking(recipe.id, multiplier) 
                    },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) },
                    text = { Text("Start Cooking") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is RecipeDetailUiState.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading recipe...", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                is RecipeDetailUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is RecipeDetailUiState.Success -> {
                    RecipeDetailContent(
                        recipe = state.recipe,
                        multiplier = multiplier,
                        onMultiplierChange = viewModel::setMultiplier,
                        onIngredientCheckedChange = viewModel::toggleIngredientChecked
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDetailContent(
    recipe: Recipe,
    multiplier: Float,
    onMultiplierChange: (Float) -> Unit,
    onIngredientCheckedChange: (Long, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = recipe.title, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Prep: ${recipe.prepTimeMinutes}m | Cook: ${recipe.cookTimeMinutes}m",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = "Scaling", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(0.5f, 1.0f, 2.0f, 4.0f).forEach { value ->
                    FilterChip(
                        selected = multiplier == value,
                        onClick = { onMultiplierChange(value) },
                        label = { Text("${value}x") }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Ingredients", style = MaterialTheme.typography.titleLarge)
        }

        items(recipe.ingredients) { ingredient ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIngredientCheckedChange(ingredient.id, !ingredient.isChecked) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = ingredient.isChecked,
                    onCheckedChange = { onIngredientCheckedChange(ingredient.id, it) }
                )
                Text(
                    text = "${ingredient.quantity} ${ingredient.unit} ${ingredient.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Instructions", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        recipe.parts.forEach { part ->
            if (!part.title.isNullOrBlank() || recipe.parts.size > 1) {
                item {
                    Text(
                        text = if (!part.title.isNullOrBlank()) "Part ${part.order}: ${part.title}" else "Part ${part.order}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            items(part.steps) { step ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(text = "Step ${step.order}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    Text(text = step.instruction)
                    if (step.stepIngredients.isNotEmpty()) {
                        Text(
                            text = "Ingredients: " + step.stepIngredients.joinToString { it.name },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
        }
    }
}
