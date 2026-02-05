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
- [ ] Define Room Entities:
    - [ ] `RecipeEntity`
    - [ ] `IngredientEntity` (Foreign key to Recipe)
    - [ ] `StepEntity` (Foreign key to Recipe)
- [ ] Create Room DAOs for CRUD operations:
    - [ ] Insert recipe with ingredients and steps (Transaction).
    - [ ] Query all recipes for list view.
    - [ ] Query full recipe details by ID.
    - [ ] Delete recipe.
- [ ] Implement Room Database class and Hilt module for Database injection.

## 3. Domain Layer & Models
- [ ] Define Domain Models:
    - [ ] `Recipe`
    - [ ] `Ingredient`
    - [ ] `Step`
- [ ] Define Repository Interfaces.
- [ ] Implement Use Cases:
    - [ ] `GetRecipesUseCase`
    - [ ] `GetRecipeByIdUseCase`
    - [ ] `AddRecipeFromUrlUseCase`
    - [ ] `DeleteRecipeUseCase`
    - [ ] `ScaleIngredientsUseCase` (Mathematical logic for 0.5x, 1x, 2x, 4x)

## 4. Recipe List Feature
- [ ] Implement `RecipeListViewModel`.
- [ ] Create `RecipeListScreen` Composable:
    - [ ] `LazyColumn` for recipe cards.
    - [ ] `RecipeCard` component (Image, Title, Time, Ingredient summary).
- [ ] Handle navigation to Recipe Overview.

## 5. Recipe Overview & Scaling Feature
- [ ] Implement `RecipeDetailViewModel`.
- [ ] Create `RecipeDetailScreen` Composable:
    - [ ] Recipe header (Image, Title).
    - [ ] Ingredient list with scaling support.
    - [ ] Scaling selector (0.5x, 1x, 2x, 4x).
    - [ ] Instructions list.
    - [ ] "Start Cooking" Floating Action Button.
    - [ ] Delete option (Menu or Button).

## 6. Cook Mode (Slideshow) Feature
- [ ] Implement `CookModeViewModel`.
- [ ] Create `CookModeScreen` Composable:
    - [ ] `HorizontalPager` for step-by-step navigation.
    - [ ] Step content: Instruction text, scaled ingredients for step, timer/duration display.
    - [ ] Navigation controls: Previous/Next buttons, swipe support, tap-to-advance.
- [ ] Implement "Keep Screen On" logic using `LocalView.current.keepScreenOn`.

## 7. URL Import Feature (Gemini Proxy)
- [ ] Define Retrofit API interface for parsing endpoint.
- [ ] Implement Repository logic to call the proxy and map response to Domain/Entity models.
- [ ] Create "Add Recipe" UI (Dialog or simple input field in List Screen).
- [ ] Implement error handling and loading states for the import process.

## 8. Initialization & Preloaded Data
- [ ] Create hardcoded data for:
    - [ ] Fluffy Pancakes
    - [ ] Spaghetti Bolognese
    - [ ] Chocolate Chip Cookies
- [ ] Implement logic to prepopulate the Room database on first app launch.
- [ ] Verify image loading for preloaded recipes (using bundled assets or URLs).

## 9. Final Polish & Testing (P0)
- [ ] Verify Material You dynamic coloring works on API 31+.
- [ ] Ensure single layout works correctly on both Phone and Tablet.
- [ ] Basic unit tests for ingredient scaling logic.
- [ ] Manual end-to-end testing of the P0 flow: Import -> View -> Scale -> Cook -> Delete.
