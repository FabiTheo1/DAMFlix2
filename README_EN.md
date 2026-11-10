# DAMFlix 🎬

**DAMFlix** is a mobile application developed for Android using modern technologies such as **Kotlin**, **Jetpack Compose**, and **Architecture Components**. Its goal is to provide an interactive, fast, and smooth movie catalog platform, implementing local data persistence with **Room**, advanced navigation with arguments, and asynchronous state management using **Coroutines**.

## 🚀 Key Features

* **Modern UI with Jetpack Compose:** 100% native and adaptive declarative design.

* **Local Persistence (Room Database):** Structured and secure movie storage using entities, DAOs, and repositories.

* **Dynamic Navigation:** Centralized routing system and parameter passing between screens (such as viewing movie details based on its ID).

* **Asynchronous Management:** Database operations executed on background threads using `Dispatchers.IO` and `withContext` to prevent UI thread blocking.

* **Robust UI States:** Implementation of sealed interfaces (`detallUiState` / `DetalleUiState`) to cleanly and predictably handle Loading, Success, and Error states.

## 📂 Project Structure

The project follows a clean architecture organized into standard Android packages:

```
com.example.damflix/
│
├── data/                        # Data layer
│   ├── local/                   # Room database (DAO, Entity, AppDatabase)
│   └── PeliculaRepository.kt    # Central data repository
│
├── ui/                          # Presentation layer
│   ├── components/              # Reusable components (Cards, etc.)
│   ├── navigation/              # Routing configuration (Ruta.kt, NavHost)
│   ├── screens/                 # Main screens (Home, List, Detail, Form)
│   ├── state/                   # UI state definitions (e.g., DetalleUiState)
│   ├── theme/                   # App colors, typography, and themes
│   └── viewmodel/               # Associated ViewModels and Factories
│
├── DAMFlixApp.kt                # Application container
└── MainActivity.kt              # Main activity

```

## 🛠️ Technologies and Libraries Used

* **Language:** Kotlin

* **UI Toolkit:** Jetpack Compose & Material 3

* **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern

* **Database:** Room

* **Concurrency:** Kotlin Coroutines & Flow

* **Navigation:** Jetpack Navigation Compose

## 📱 Application Screens

1. **Home Screen (`PantallaInicioScreen`):** Welcome and main entry point to the application.

2. **Movie Catalog (`ListaPeliculasScreen`):** Grid/list view of movies stored locally.

3. **Movie Detail (`DetallePeliculaScreen`):** Detailed view displaying extended information (Title, Director, Genre, Release Year, and Synopsis) fetched asynchronously using the movie identifier.

4. **Form (`FormularioPeliculaScreen`):** Screen intended for registering and adding new movies to the local database.

## ⚙️ Setup and Execution

1. Clone the repository to your local machine.

2. Open the project using **Android Studio** (Hedgehog version or higher recommended).

3. Sync the project with Gradle files (`build.gradle.kts`).

4. Run the application on an emulator with API 24 or higher (or on a physical device with USB debugging enabled).

## 📄 License

This project is distributed under the terms of the MIT License. Check the `LICENSE` file for more details.