# Technical Design Document: RecipeViewer

## 1. Introduction
This document outlines the technical architecture and design for the RecipeViewer Android application. The app allows users to manage recipes, import them from URLs, and provides an interactive "Cook Mode" for step-by-step guidance.

## 2. Architecture Overview
The application will follow **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern to ensure scalability, maintainability, and testability.

### 2.1 Layers
*   **UI Layer (Compose):** Contains ViewModels and Composable functions.
*   **Domain Layer:** Contains Use Cases and Domain Models (Business Logic).
*   **Data Layer:** Contains Repositories, Data Sources (Room, Retrofit), and Data Mappers.

## 3. Technology Stack
*   **Language:** Kotlin
*   **Minimum SDK:** 31 (Android 12)
*   **UI Framework:** Jetpack Compose
*   **Design System:** Material 3 (Material You) with Dynamic Coloring
*   **Database:** Room (SQLite)
*   **Networking:** Retrofit with OkHttp (for Gemini Proxy API)
*   **Dependency Injection:** Hilt
*   **Asynchronous Programming:** Kotlin Coroutines & Flow
*   **Image Loading:** Coil
*   **Navigation:** Jetpack Compose Navigation

## 4. Data Models

### 4.1 Domain Models
```kotlin
data class Recipe(
    val id: Long = 0,
    val title: String,
    val imageUrl: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val ingredients: List<Ingredient>,
    val steps: List<Step>,
    val servings: Int = 1
)

data class Ingredient(
    val name: String,
    val quantity: Double,
    val unit: String
)

data class Step(
    val order: Int,
    val instruction: String,
    val stepIngredients: List<Ingredient>,
    val durationMinutes: Int? = null
)
```

## 5. Database Design (Room)
The database will consist of three main tables:
1.  **RecipesTable**: Basic info (title, image, times).
2.  **IngredientsTable**: Foreign key to Recipe ID.
3.  **StepsTable**: Foreign key to Recipe ID, includes order and instruction.

## 6. Component Design

### 6.1 Recipe List Screen
*   Uses a `LazyColumn` to display recipe cards.
*   Each card displays the title, image, total time, and an ingredient summary.

### 6.2 Recipe Detail Screen
*   Displays the full list of ingredients and steps.
*   Includes a "Start Cooking" FAB or button.
*   Provides a Scaling Selector (0.5x, 1x, 2x, 4x) which triggers UI updates via the ViewModel.

### 6.3 Cook Mode (Slideshow)
*   Implemented using a `HorizontalPager` for swipe gestures and step navigation.
*   **Keep Screen On:** Uses `LocalView.current.keepScreenOn = true` while the Composable is active.
*   **Bottom Navigation:**
    *   Uses a `BottomAppBar` or a simple `Row` at the bottom of the screen.
    *   Contains "Previous" and "Next" `FilledTonalButton` or `IconButton` components.
    *   Buttons use `Icons.AutoMirrored.Filled.ArrowBack` and `Icons.AutoMirrored.Filled.ArrowForward`.
    *   Buttons are enabled/disabled based on the current page index in `pagerState`.
*   Displays specific ingredients and duration for the current step.

### 6.4 URL Import
*   A dialog or dedicated screen to paste a URL.
*   Calls the Gemini-powered proxy endpoint.
*   Parses the JSON response into a `Recipe` object and saves it to Room.

## 7. Key Features Implementation Details

### 7.1 Ingredient Scaling
Scaling logic resides in the ViewModel or a Use Case:
`scaledQuantity = originalQuantity * multiplier`
The UI observes a `StateFlow` of the recipe which is updated when the multiplier changes.

### 7.2 Preloaded Data
On the first app launch (checked via DataStore or a database empty check), the app will populate the Room database with the three P0 recipes (Pancakes, Spaghetti, Cookies).

### 7.3 Material You & Dynamic Coloring
The app will use `dynamicLightColorScheme` and `dynamicDarkColorScheme` (API 31+) to adapt to the user's wallpaper.

## 8. API Integration
**Endpoint:** `POST /parse-recipe`
**Request:** `{ "url": "string" }`
**Response:** JSON representing the recipe schema (matching the Domain Model).

## 9. Error Handling
*   Repository layer will catch network and database exceptions and wrap them in a `Result` or `Resource` class.
*   UI will display user-friendly Toast messages or SnackBar for failures (e.g., "Failed to add recipe").
