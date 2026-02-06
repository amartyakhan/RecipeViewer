# RecipeViewer Implementation Task List (P0 & P1)

This document tracks the progress of P0 and P1 requirements implementation for the RecipeViewer application.

## 1. Project Setup & Infrastructure (P0)
- [x] Configure `build.gradle.kts` with necessary dependencies:
    - [x] Compose (Material 3, Navigation, Tooling)
    - [x] Hilt (Dependency Injection)
    - [x] Room (Persistence)
    - [x] Google AI Client SDK (for Gemini)
    - [x] Jsoup (HTML Parsing)
    - [x] Secrets Gradle Plugin (API Key management)
    - [x] Coil (Image Loading)
    - [x] Serialization (Kotlinx Serialization)
- [x] Set up Hilt Application class and Manifest entry.
- [x] Define Material 3 Theme with Dynamic Color support (API 31+).
- [x] Implement Navigation Graph (List -> Overview -> Cook Mode).

## 2. Data Layer Implementation (Room & Persistence) (P0)
- [x] Define Room Entities:
    - [x] `RecipeEntity`
    - [x] `IngredientEntity` (Foreign key to Recipe)
    - [ ] **Recipe Part Support:** `RecipePartEntity` (Foreign key to Recipe)
    - [x] `StepEntity` (Foreign key to Recipe) -> *Update to include partId*
    - [x] `StepIngredientEntity` (Junction table for step-specific ingredients)
- [x] Create Room DAOs for CRUD operations:
    - [ ] Update Transaction to include `RecipePartEntity` insertion.
    - [x] Support inserting and querying step-ingredient relationships.
    - [x] Query all recipes for list view.
    - [x] Query full recipe details by ID (including parts).
    - [x] Delete recipe.
- [x] Implement Room Database class and Hilt module for Database injection.

## 3. Domain Layer & Models (P0)
- [x] Define Domain Models:
    - [x] `Recipe` -> *Update to include parts list*
    - [x] `Ingredient`
    - [ ] `RecipePart`
    - [x] `Step`
- [x] Define Repository Interfaces.
- [x] Implement Use Cases:
    - [x] `GetRecipesUseCase`
    - [x] `GetRecipeByIdUseCase`
    - [x] `AddRecipeFromUrlUseCase`
    - [x] `DeleteRecipeUseCase`
    - [x] `ScaleIngredientsUseCase` (Mathematical logic for 0.5x, 1x, 2x, 4x)

## 4. Recipe List Feature (P0)
- [x] Implement `RecipeListViewModel`.
- [x] Create `RecipeListScreen` Composable:
    - [x] `LazyColumn` for recipe cards.
    - [x] `RecipeCard` component (Image, Title, Time, Ingredient summary).
    - [x] **Add Recipe FAB (P0):** A Floating Action Button to trigger the manual URL entry flow.
- [x] Handle navigation to Recipe Overview.

## 5. Recipe Overview & Scaling Feature (P0)
- [x] Implement `RecipeDetailViewModel`.
- [x] Create `RecipeDetailScreen` Composable:
    - [x] Recipe header (Image, Title).
    - [x] Ingredient list with scaling support.
    - [x] Scaling selector (0.5x, 1x, 2x, 4x).
    - [ ] **Grouped Instructions:** Instructions list grouped by `RecipePart`.
    - [x] "Start Cooking" Floating Action Button.
    - [x] Delete option (Menu or Button).

## 6. Cook Mode (Slideshow) Feature (P0)
- [x] Implement `CookModeViewModel`.
- [x] Create `CookModeScreen` Composable:
    - [x] `HorizontalPager` for step-by-step navigation.
    - [ ] **Part Info:** Display current Part number/title in the step page.
    - [x] Update `StepPage` to display specific ingredients for each step, dynamically scaled.
    - [x] Add explicit "Previous" and "Next" navigation buttons at the bottom using Material 3 styles.
- [x] Implement "Keep Screen On" logic using `LocalView.current.keepScreenOn`.

## 7. URL Import Feature (Gemini SDK)
- [x] Set up Gemini API Key in `local.properties` (P0).
- [x] **Manual Entry UI (P0):**
    - [x] Implement a Dialog or BottomSheet for URL input.
    - [x] Add URL validation logic.
    - [x] Add "Get Recipe" button that triggers the pipeline upon validation.
- [x] **Scraping Pipeline (P0):** Integrate Jsoup to extract text content from the `<article>` or `<body>` tag of the provided URL.
- [ ] **LLM Extraction (P0):** Update `RecipeExtractionDataSource` to identify and group steps into parts in the JSON response.
- [x] **Image URL Fallback (P0):** If Gemini response lacks an image URL, use Jsoup to parse the original HTML for a suitable image (e.g., from OpenGraph tags).
- [x] Implement Repository logic to map Gemini's JSON response to Domain Models and persist to Room (P0).
- [x] **Loading & Feedback UI (P0):**
    - [x] Implement loading, success, and failure states within the "Add Recipe" dialog (moved to a separate feedback dialog).
    - [x] Display success/failure icons and messages in the dialog instead of Toasts.
    - [x] Include the recipe name in the success message (e.g., "Successfully added: [Name]").
    - [x] Implement a "Close" or "OK" button to dismiss the dialog after the process completes.
    - [x] Implement **30-second timeout** and display "Request timed out" message in the dialog.
- [ ] **Intercept Shared URL (P1):** Add `intent-filter` to `MainActivity` in `AndroidManifest.xml` for `ACTION_SEND` (text/plain) to handle URLs shared from browsers.

## 8. Initialization & Preloaded Data (P0)
- [x] Create hardcoded data for:
    - [x] Fluffy Pancakes
    - [x] Spaghetti Bolognese
    - [x] Chocolate Chip Cookies
- [ ] **Part Support:** Update `PreloadData` to group steps into parts.
- [x] Map preloaded ingredients to their respective steps in `PreloadData`.
- [x] Implement logic to prepopulate the Room database on first app launch.
- [x] Verify image loading for preloaded recipes (using bundled assets or URLs).

## 9. Final Polish & Testing
- [ ] Verify Material You dynamic coloring works on API 31+ (P0).
- [ ] Ensure single layout works correctly on both Phone and Tablet (P0).
- [ ] Basic unit tests for ingredient scaling logic (P0).
- [ ] Manual end-to-end testing of the P0 flow: Manual Import -> View -> Scale -> Cook -> Delete (P0).
- [ ] Manual testing of Share Sheet Import flow (P1).
