# Technical Design Document: RecipeViewer

## 1. Introduction
This document outlines the technical architecture and design for the RecipeViewer Android application. The app allows users to manage recipes, import them from URLs, and provides an interactive "Cook Mode" for step-by-step guidance.

## 2. Architecture Overview
The application will follow **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern to ensure scalability, maintainability, and testability.

### 2.1 Layers
*   **UI Layer (Compose):** Contains ViewModels and Composable functions.
*   **Domain Layer:** Contains Use Cases and Domain Models (Business Logic).
*   **Data Layer:** Contains Repositories, Data Sources (Room, Gemini AI SDK), and Data Mappers.

## 3. Technology Stack
*   **Language:** Kotlin
*   **Minimum SDK:** 31 (Android 12)
*   **UI Framework:** Jetpack Compose
*   **Design System:** Material 3 (Material You) with Dynamic Coloring
*   **Database:** Room (SQLite)
*   **Extraction Engine:** Google AI Client SDK (`com.google.ai.client.generativeai`) for direct communication with Gemini Pro/Flash.
*   **HTML Parsing:** Jsoup (for pre-processing URLs to extract text content).
*   **Security:** Secrets Gradle Plugin (to manage Gemini API keys in `local.properties`).
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
    val id: Long = 0,
    val name: String,
    val quantity: Double,
    val unit: String
)

data class Step(
    val id: Long = 0,
    val order: Int,
    val instruction: String,
    val stepIngredients: List<Ingredient>,
    val durationMinutes: Int? = null
)
```

## 5. Database Design (Room)
The database will consist of the following tables:
1.  **RecipesTable**: Basic info (title, image, times, servings).
2.  **IngredientsTable**: List of all ingredients for a recipe. Foreign key to Recipe ID.
3.  **StepsTable**: List of cooking steps. Foreign key to Recipe ID.
4.  **StepIngredientsTable**: A junction table linking Steps and Ingredients to specify which ingredients (and their exact quantities) are used in which step.
    *   `stepId`: Foreign key to StepsTable.
    *   `ingredientId`: Foreign key to IngredientsTable.

## 6. Component Design

### 6.1 Recipe List Screen
*   Uses a `LazyColumn` to display recipe cards.
*   Each card displays the title, image, total time, and an ingredient summary.
*   A Floating Action Button (FAB) (P0) initiates the "Add Recipe by URL" flow.

### 6.2 Add Recipe Dialog (P0)
*   Opens when the user clicks the FAB.
*   Contains a `TextField` for the user to paste the recipe URL.
*   Validates the input to ensure it is a well-formed URL.
*   A "Get Recipe" button is enabled only when a valid URL is present.
*   Clicking the button triggers the `AddRecipeFromUrlUseCase`.
*   **Loading UI:** While the extraction is in progress, the dialog displays a `CircularProgressIndicator`.
*   **Interaction Blocking:** The background UI is disabled/unresponsive until the extraction process completes or fails.

### 6.3 Recipe Detail Screen
*   Displays the full list of ingredients and steps.
*   Includes a "Start Cooking" FAB or button.
*   Provides a Scaling Selector (0.5x, 1x, 2x, 4x) which triggers UI updates via the ViewModel.

### 6.4 Cook Mode (Slideshow)
*   Implemented using a `HorizontalPager` for swipe gestures and step navigation.
*   **Keep Screen On:** Uses `LocalView.current.keepScreenOn = true` while the Composable is active.
*   **Bottom Navigation:**
    *   Contains "Previous" and "Next" buttons at the bottom.
    *   Buttons are enabled/disabled based on the current page index.
*   **Step Content:**
    *   Displays the instruction text for the current step.
    *   Displays a list of **exact ingredients and quantities** required for the current step, dynamically scaled based on the selected multiplier.
    *   Displays the duration for the step if available.

### 6.5 URL Import Pipeline
1.  **Input:** 
    *   **Manual Entry (P0):** The feature is triggered by manual URL entry in the "Add Recipe Dialog".
    *   **Share Sheet (P1):** Integration with Android's Share Sheet via an `intent-filter` in `AndroidManifest.xml` for `ACTION_SEND`.
2.  **Scraping:** Uses Jsoup to extract text content from the `<article>` or `<body>` tag of the URL.
3.  **Extraction:** Sends the extracted text to Gemini via the Google AI Client SDK.
    *   **Timeout Implementation:** The call to Gemini will be wrapped in a `withTimeout(30000)` block in the repository/datasource layer to ensure the app doesn't wait indefinitely.
4.  **Structured Output:** The Generative Model is configured with `responseMimeType = "application/json"` to ensure a parseable JSON response.
5.  **Persistence:** The JSON is parsed into the `Recipe` domain model and saves it to Room.

## 7. Key Features Implementation Details

### 7.1 Ingredient Scaling
Scaling logic resides in the `ScaleIngredientsUseCase`:
`scaledQuantity = originalQuantity * multiplier`
This scaling is applied to both the main ingredient list in the Detail Screen and the step-specific ingredient lists in Cook Mode.

### 7.2 Preloaded Data
The app populates the Room database with three P0 recipes. The `PreloadData` will be updated to include the mapping of ingredients to their respective steps.

### 7.3 Material You & Dynamic Coloring
The app will use `dynamicLightColorScheme` and `dynamicDarkColorScheme` (API 31+) to adapt to the user's wallpaper.

## 8. Gemini Integration
*   **Model:** Gemini 1.5 Flash (optimized for speed and structured output).
*   **Prompting:** Strict system instructions to return JSON matching the app's internal schema.
*   **API Key Management:** API key is retrieved via `BuildConfig` (populated by Secrets Gradle Plugin).

## 9. Error Handling
*   Repository layer will catch network, AI SDK, and database exceptions and wrap them in a `Result` or `Resource` class.
*   UI will display user-friendly Toast messages based on the specific error encountered:
    *   **HTTP 503:** "Service not available. Please try again later."
    *   **Timeout (30s):** "Request timed out. Please try again."
    *   **Invalid URL:** "The URL provided is invalid."
    *   **Parsing Failure:** "Failed to parse recipe from this website."
    *   **General Error:** "An unexpected error occurred."
