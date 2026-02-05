package com.example.recipeviewer.ui.recipe_list

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.recipeviewer.domain.model.Recipe
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    onRecipeClick: (Long) -> Unit,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(viewModel.scrapeResult) {
        viewModel.scrapeResult.collectLatest { result ->
            result.onSuccess { text ->
                Toast.makeText(context, "Extracted text: ${text.take(100)}...", Toast.LENGTH_LONG).show()
            }.onFailure { error ->
                Toast.makeText(context, "Failed to scrape: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Recipes") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is RecipeListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is RecipeListUiState.Success -> {
                    if (state.recipes.isEmpty()) {
                        Text(
                            text = "No recipes yet. Add one!",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.recipes) { recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onClick = { onRecipeClick(recipe.id) }
                                )
                            }
                        }
                    }
                }
                is RecipeListUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        if (showAddDialog) {
            AddRecipeDialog(
                onDismiss = { showAddDialog = false },
                onGetRecipe = { url ->
                    showAddDialog = false
                    viewModel.onGetRecipe(url)
                }
            )
        }
    }
}

@Composable
fun AddRecipeDialog(
    onDismiss: () -> Unit,
    onGetRecipe: (String) -> Unit
) {
    var url by remember { mutableStateOf("") }
    val isValidUrl = remember(url) {
        url.isNotBlank() && Patterns.WEB_URL.matcher(url).matches()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Recipe by URL") },
        text = {
            Column {
                Text("Paste a recipe URL below to extract its details.")
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Recipe URL") },
                    placeholder = { Text("https://example.com/recipe") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onGetRecipe(url) },
                enabled = isValidUrl
            ) {
                Text("Get Recipe")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${recipe.ingredients.size} ingredients",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${recipe.prepTimeMinutes + recipe.cookTimeMinutes} mins",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
