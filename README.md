# RecipeViewer

RecipeViewer is a modern Android application designed to help you organize and follow your favorite recipes with ease. It leverages Google's Gemini AI to extract recipe information directly from web URLs, providing a seamless way to grow your collection.

## Features

- **AI-Powered Recipe Extraction:** Add recipes simply by pasting a URL. The app uses the Google AI SDK (Gemini 1.5 Flash) to parse websites and extract structured recipe data.
- **Cook Mode:** A dedicated step-by-step slideshow mode that keeps your screen on while you cook. Navigate through instructions with ease using buttons or gestures.
- **Ingredient Scaling:** Adjust recipe quantities on the fly with support for 0.5x, 1x, 2x, and 4x multipliers.
- **Ingredient Checklist:** Keep track of your progress by marking ingredients as gathered or prepared, synced across the overview and Cook Mode.
- **Material You Design:** A beautiful, modern UI built with Material 3, supporting dynamic coloring on Android 12+.
- **Local Storage:** All your recipes are stored securely on your device using a Room database.

## Screenshots
 To be added.

## Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Database:** [Room](https://developer.android.com/training/data-storage/room)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/)
- **AI Integration:** [Google AI SDK for Android (Gemini)](https://ai.google.dev/android/sdk)
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
- **HTML Parsing:** [Jsoup](https://jsoup.org/) (for fallback image extraction)

## Requirements

- Android 12 (API level 31) or later.

## Getting Started

1. Clone this repository.
2. Obtain a Gemini API Key from the [Google AI Studio](https://aistudio.google.com/).
3. Add your API key to the project (e.g., via `local.properties` or a secrets plugin configuration).
4. Build and run the app on an Android device or emulator running API 31+.

## License

*(Add your license information here, e.g., MIT, Apache 2.0)*
