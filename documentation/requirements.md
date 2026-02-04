# RecipeViewer Requirements

This document outlines the functional and non-functional requirements for the RecipeViewer Android application.

## 1. Core Functionality
### 1.1 Recipe Data Sourcing & Storage
*   **P0:** Recipes and their images are stored locally in a **SQLite/Room database** on the user's device.
*   **P0:** App starts with **3 preloaded recipes** to demonstrate the Cook Mode experience:
    1.  **Fluffy Pancakes** (Breakfast)
    2.  **Spaghetti Bolognese** (Main Dish)
    3.  **Chocolate Chip Cookies** (Dessert)
*   **P0:** Users can add new recipes by providing a **recipe URL** (manually pasted into the app).
    *   The app calls a **Server-Side Proxy endpoint** that wraps a Gemini LLM call to parse the URL.
    *   The API returns structured recipe data compatible with the app's schema.
    *   The app stores the parsed recipe in the local database.
*   **P0 Error Handling:** Simple "Failed to add recipe" message if parsing fails or URL is invalid.
*   **P0:** Users can **delete any recipe** (including preloaded ones) from the recipe details/view screen.
*   **P1:** Recipes and images will be stored and synced via a custom cloud backend service.
*   **P1:** Android **"Share" menu integration**: Users can share a URL directly from a browser to RecipeViewer.
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
        *   Previous and Next buttons.
        *   Swipe gestures (left/right) for back and forth.
        *   Tap to move forward to the next step.
    *   **Content per step:**
        *   One instruction step.
        *   Specific ingredients and quantities for that step.
        *   Time duration for that specific step.
*   **P1:** Simple visual timer for steps with a duration.
*   **P2:** Notification/Alarm when the timer ends.

### 2.4 Ingredient Scaling & Servings
*   **P0:** Support for scaling servings with fixed multipliers: 0.5x (1/2), 1x, 2x, and 4x.
*   **P0:** Apply simple mathematical scaling logic to ingredient quantities.
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
