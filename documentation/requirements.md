# RecipeViewer Requirements

This document outlines the functional and non-functional requirements for the RecipeViewer Android application.

## 1. Core Functionality
### 1.1 Recipe Data Sourcing & Storage
*   **P0:** Recipes and their images are stored locally in a **SQLite/Room database** on the user's device.
*   **P0:** App starts with **3 preloaded recipes** to demonstrate the Cook Mode experience:
    1.  **Fluffy Pancakes** (Breakfast)
    2.  **Spaghetti Bolognese** (Main Dish)
    3.  **Chocolate Chip Cookies** (Dessert)
*   **P0:** Users can add new recipes by manually providing a **recipe URL**.
    *   **Manual Entry (P0):** 
        *   A Floating Action Button (FAB) on the home screen allows users to add a new recipe.
        *   Clicking the FAB opens a text input field for the URL.
        *   If a valid URL is entered, a "Get Recipe" button becomes available.
        *   Clicking "Get Recipe" triggers the extraction pipeline.
        *   **Loading State:** While the Gemini API is processing the request, a loading UI is displayed on the input popup, and the background UI is disabled to prevent concurrent actions.
    *   **Extraction:** The app uses the **Google AI Client SDK for Android** to call a cloud-based Gemini model (e.g., Gemini 1.5 Flash) to parse the URL content.
    *   **Persistence:** The model returns structured JSON recipe data compatible with the app's schema, which is then stored in the local database.
*   **P0 Error Handling:** 
    *   Display specific **Toast notifications** based on the extraction result or API response code.
    *   Example: 503 error should specifically notify "Service not available".
    *   **Timeout (P0):** If the Gemini API response takes longer than **30 seconds**, the request should be cancelled, and a "Request timed out. Please try again." Toast should be shown.
    *   General "Failed to add recipe" message for other unexpected failures or invalid URL parsing.
*   **P0:** Users can **delete any recipe** (including preloaded ones) from the recipe details/view screen.
*   **P1:** **Share Sheet Integration:** Users can share a URL directly from a browser or other apps to RecipeViewer using the Android Share Sheet.
*   **P1:** Recipes and images will be stored and synced via a custom cloud backend service.
*   **P2:** Manual management (create from scratch, edit) of personal recipes.
*   **P2:** **Gemini Multimodal parsing**: Support for parsing recipes from images or screenshots.

### 1.2 Localization & Languages
*   **P0:** English only.
*   **P1:** Support for multiple languages (UI localization).

### 1.3 UI/UX & Platform
*   **P0:** Single layout design for both phones and tablets (no specific tablet optimization).
*   **P0:** **Minimum Android Version: API 31 (Android 12) or later**.
*   **P0:** Leverage **Material You** (Material 3) features, including dynamic coloring.
*   **P1:** Dark mode support.

## 2. Features & User Experience
### 2.1 Recipe List View
*   **P0 (Must-Have):** Display a list of available recipes.
*   **P0 (Must-Have):** Each list item must show:
    *   Recipe Title.
    *   Recipe Image.
    *   Total Preparation and Cooking Time.
    *   Main Ingredients (summary).

### 2.2 Recipe Overview Screen
*   **P0 (Must-Have):** Displayed when a user selects a recipe from the list.
*   **P0 (Must-Have):** This screen shows:
    *   Full list of all ingredients and their quantities.
    *   Full list of all cooking instructions/steps.
*   **P0 (Must-Have):** Contains a "Start Cooking" button to launch the Cook Mode slideshow.
*   **P0 (Must-Have):** Contains the "Delete" button for the recipe.

### 2.3 Cook Mode (Slideshow)
*   **P0 (Must-Have):** Step-by-step instruction slideshow.
    *   **Screen Behavior:** Screen stays awake (Keep Screen On) while in Cook Mode.
    *   **Navigation:**
        *   **Bottom Navigation Bar:** Explicit "Previous" and "Next" buttons at the bottom of the screen using Material Design arrow icons.
        *   Swipe gestures (left/right) for back and forth.
        *   Tap to move forward to the next step (optional).
    *   **Content per step:**
        *   One instruction step.
        *   **Exact quantities** of specific ingredients used in that step (dynamically scaled).
        *   Time duration for that specific step.
*   **P1:** Simple visual timer for steps with a duration.
*   **P2:** Notification/Alarm when the timer ends.

### 2.4 Ingredient Scaling & Servings
*   **P0:** Support for scaling servings with fixed multipliers: 0.5x (1/2), 1x, 2x, and 4x.
*   **P0:** Apply simple mathematical scaling logic to ingredient quantities in both the Overview and Cook Mode screens.
*   **P1:** "Main Ingredient" scaling: Users can specify the exact quantity of the primary ingredient they have (e.g., 750g of chicken), and the app will proportionally scale all other ingredients.

### 2.5 Navigation & App Structure
*   **P0:** Hierarchical navigation (List -> Overview -> Cook Mode) using the system back button.
*   **P1:** Bottom Navigation Bar for major sections (e.g., Recipes, Favorites, Search).
*   **P2:** Side Navigation Drawer for supplementary items (e.g., Settings, Shopping List).

### 2.6 Additional Features
*   **P1:** Search bar to find recipes by name.
*   **P1:** Categorization (e.g., Breakfast, Dinner, Desserts).
*   **P1:** Favorites/Bookmarks system.
*   **P2:** Shopping list generator from recipe ingredients.
