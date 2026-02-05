# RecipeViewer Implementation Task List (P0)

This document tracks the progress of P0 requirements implementation for the RecipeViewer application.

## 1. Project Setup & Infrastructure
- [x] Configure `build.gradle.kts` with necessary dependencies:
    - [x] Compose (Material 3, Navigation, Tooling)
    - [x] Hilt (Dependency Injection)
    - [x] Room (Persistence)
    - [x] Retrofit & OkHttp (Networking)
    - [x] Coil (Image Loading)
    - [x] Serialization (Kotlinx Serialization)
- [x] Set up Hilt Application class and Manifest entry.
- [x] Define Material 3 Theme with Dynamic Color support (API 31+).
- [x] Implement Navigation Graph (List -> Overview -> Cook Mode).

## 2. Data Layer Implementation (Room & Persistence)
- [x] Define Room Entities:
    - [x] `RecipeEntity`
    - [x] `IngredientEntity` (Foreign key to Recipe)
    - [x] `StepEntity` (Foreign key to Recipe)
    - [ ] `StepIngredientEntity` (Junction table for step-specific ingredients)
- [x] Create Room DAOs for CRUD operations:
    - [x] Insert recipe with ingredients and steps (Transaction).
    - [ ] Support inserting and querying step-ingredient relationships.
    - [x] Query all recipes for list view.
    - [x] Query full recipe details by ID.
    - [x] Delete recipe.
- [x] Implement Room Database class and Hilt module for Database injection.

## 3. Domain Layer & Models
- [x] Define Domain Models:
    - [x] `Recipe`
    - [x] `Ingredient`
    - [x] `Step`
- [x] Define Repository Interfaces.
- [x] Implement Use Cases:
    - [x] `GetRecipesUseCase`
    - [x] `GetRecipeByIdUseCase`
    - [ ] `AddRecipeFromUrlUseCase` (Pending Network Layer)
    - [x] `DeleteRecipeUseCase`
    - [x] `ScaleIngredientsUseCase` (Mathematical logic for 0.5x, 1x, 2x, 4x)

## 4. Recipe List Feature
- [x] Implement `RecipeListViewModel`.
- [x] Create `RecipeListScreen` Composable:
    - [x] `LazyColumn` for recipe cards.
    - [x] `RecipeCard` component (Image, Title, Time, Ingredient summary).
- [x] Handle navigation to Recipe Overview.

## 5. Recipe Overview & Scaling Feature
- [x] Implement `RecipeDetailViewModel`.
- [x] Create `RecipeDetailScreen` Composable:
    - [x] Recipe header (Image, Title).
    - [x] Ingredient list with scaling support.
    - [x] Scaling selector (0.5x, 1x, 2x, 4x).
    - [x] Instructions list.
    - [x] "Start Cooking" Floating Action Button.
    - [x] Delete option (Menu or Button).

## 6. Cook Mode (Slideshow) Feature
- [x] Implement `CookModeViewModel`.
- [x] Create `CookModeScreen` Composable:
    - [x] `HorizontalPager` for step-by-step navigation.
    - [ ] Update `StepPage` to display specific ingredients for each step, dynamically scaled.
    - [x] Add explicit "Previous" and "Next" navigation buttons at the bottom using Material 3 styles.
- [x] Implement "Keep Screen On" logic using `LocalView.current.keepScreenOn`.

## 7. URL Import Feature (Gemini Proxy)
- [ ] Define Retrofit API interface for parsing endpoint.
- [ ] Implement Repository logic to call the proxy and map response to include step-ingredient relationships.
- [ ] Create "Add Recipe" UI (Dialog or simple input field in List Screen).
- [ ] Implement error handling and loading states for the import process.

## 8. Initialization & Preloaded Data
- [x] Create hardcoded data for:
    - [x] Fluffy Pancakes
    - [x] Spaghetti Bolognese
    - [x] Chocolate Chip Cookies
- [ ] Map preloaded ingredients to their respective steps in `PreloadData`.
- [x] Implement logic to prepopulate the Room database on first app launch.
- [x] Verify image loading for preloaded recipes (using bundled assets or URLs).

## 9. Final Polish & Testing (P0)
- [ ] Verify Material You dynamic coloring works on API 31+.
- [ ] Ensure single layout works correctly on both Phone and Tablet.
- [ ] Basic unit tests for ingredient scaling logic.
- [ ] Manual end-to-end testing of the P0 flow: Import -> View -> Scale -> Cook -> Delete.
