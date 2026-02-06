package com.example.recipeviewer.ui.cook_mode

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipeviewer.domain.model.Recipe
import com.example.recipeviewer.domain.model.Step
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CookModeScreen(
    onClose: () -> Unit,
    viewModel: CookModeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Keep screen on while in Cook Mode
    val view = LocalView.current
    DisposableEffect(view) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val title = (uiState as? CookModeUiState.Success)?.recipe?.title ?: "Cook Mode"
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is CookModeUiState.Loading -> CircularProgressIndicator()
                is CookModeUiState.Error -> Text(state.message)
                is CookModeUiState.Success -> {
                    CookModeContent(recipe = state.recipe)
                }
            }
        }
    }
}

data class FlattenedStep(
    val step: Step,
    val partOrder: Int,
    val partTitle: String?,
    val totalParts: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CookModeContent(recipe: Recipe) {
    val flattenedSteps = remember(recipe) {
        recipe.parts.flatMap { part ->
            part.steps.map { step ->
                FlattenedStep(
                    step = step,
                    partOrder = part.order,
                    partTitle = part.title,
                    totalParts = recipe.parts.size
                )
            }
        }
    }

    val pagerState = rememberPagerState(pageCount = { flattenedSteps.size })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        LinearProgressIndicator(
            progress = { 
                if (flattenedSteps.isNotEmpty()) {
                    (pagerState.currentPage + 1).toFloat() / flattenedSteps.size
                } else 0f
            },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            StepPage(flattenedStep = flattenedSteps[pageIndex])
        }

        // Navigation Buttons
        Surface(
            tonalElevation = 3.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    enabled = pagerState.currentPage > 0
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Previous")
                }

                FilledTonalButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    enabled = pagerState.currentPage < flattenedSteps.size - 1
                ) {
                    Text("Next")
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun StepPage(flattenedStep: FlattenedStep) {
    val step = flattenedStep.step
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (flattenedStep.totalParts > 1 || !flattenedStep.partTitle.isNullOrBlank()) {
            val partText = if (!flattenedStep.partTitle.isNullOrBlank()) {
                "Part ${flattenedStep.partOrder}: ${flattenedStep.partTitle}"
            } else {
                "Part ${flattenedStep.partOrder}"
            }
            Text(
                text = partText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = "Step ${step.order}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = step.instruction,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium
        )

        if (step.stepIngredients.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Ingredients for this step:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            step.stepIngredients.forEach { ingredient ->
                Text(
                    text = "• ${ingredient.quantity} ${ingredient.unit} ${ingredient.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        step.durationMinutes?.let { duration ->
            Spacer(modifier = Modifier.height(32.dp))
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Timer: $duration min",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
