# Smart Pantry Manager — Android Studio Project



Smart Pantry Manager is a native Java Android application for reducing food waste. It stores the ingredients a user already has and suggests only recipes that can be completed without buying anything else.



## Features



The application provides pantry Create, Read, Update, and Delete functionality; a persistent SQLite database; a RecyclerView pantry list with a custom adapter; 18 pre-seeded recipes; strict ingredient-and-quantity recipe matching; recipe details; input validation; and a settings screen with a persisted expiring-soon reminder preference.



## Database choice



SQLite was selected because the assignment is a local pantry-management application and the module covers Android persistent storage. SQLiteOpenHelper keeps the project self-contained, works offline, persists data across restarts, and is straightforward to explain during the demonstration.



## Strict matching



A recipe is suggested only when every required ingredient is found in the pantry and the pantry quantity is at least the required quantity. Names are normalized for case, whitespace, and common plural endings, so simple differences such as `tomato` and `tomatoes` are handled.



## Run in Android Studio



Open the `SmartPantryManager` folder in Android Studio, allow Gradle to sync, select an Android emulator or USB-connected device, and press **Run**. The project is a native Java Android Studio application. It uses Java 17 source compatibility, Android SDK 35, minimum SDK 24, AndroidX, Material, and RecyclerView.



## Suggested demonstration flow



Add `eggs`, `tomatoes`, and `onions` with sufficient quantities, open Suggested Recipes, and open Tomato Omelette. Delete one ingredient and refresh Suggestions to prove the recipe disappears. Edit an ingredient to demonstrate Update, close and reopen the app to prove persistence, and open Settings to show the saved preference.



## Assignment note



The required GitHub repository and the 5–7 minute narrated video must be created by the student as part of the evidence portfolio. Meaningful incremental commits should be made throughout development rather than uploaded as one final commit.
