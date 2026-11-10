# DAMFlix 🎬

**DAMFlix** es una aplicación móvil desarrollada para Android utilizando tecnologías modernas como **Kotlin**, **Jetpack Compose** y **Architecture Components**. Su objetivo es proporcionar una plataforma de catálogo de películas interactiva, rápida y fluida, implementando persistencia local de datos con **Room**, navegación avanzada con argumentos y gestión de estados asíncronos mediante **Corrutinas**.

---

## 🚀 Características Principales

- **Interfaz Moderna con Jetpack Compose:** Diseño declarativo 100% nativo y adaptativo.
- **Persistencia Local (Room Database):** Almacenamiento estructurado y seguro de películas mediante entidades, DAOs y repositorios.
- **Navegación Dinámica:** Sistema de rutas centralizado y paso de parámetros entre pantallas (como el detalle de una película a partir de su ID).
- **Gestión Asíncrona:** Operaciones de base de datos ejecutadas en hilos secundarios utilizando `Dispatchers.IO` y `withContext` para evitar bloqueos en la interfaz de usuario.
- **Estados de UI Robustos:** Implementación de interfaces selladas (`sealed interface DetalleUiState`) para manejar estados de Carga, Éxito y Error de manera limpia y predecible.

---

## 📂 Estructura del Proyecto

El proyecto sigue una estructura limpia orientada a paquetes estándar de Android:

```text
com.example.damflix/
│
├── data/                        # Capa de datos
│   ├── local/                   # Base de datos Room (DAO, Entity, AppDatabase)
│   └── PeliculaRepository.kt    # Repositorio central de datos
│
├── ui/                          # Capa de presentación
│   ├── components/              # Componentes reutilizables (Tarjetas, etc.)
│   ├── navigation/              # Configuración de rutas (Ruta.kt, NavHost)
│   ├── screens/                 # Pantallas principales (Inicio, Lista, Detalle, Formulario)
│   ├── state/                   # Definición de estados de UI (Ej. DetalleUiState)
│   ├── theme/                   # Colores, tipografías y temas de la app
│   └── viewmodel/               # ViewModels y Factories asociados
│
├── DAMFlixApp.kt                # Contenedor de la aplicación
└── MainActivity.kt              # Actividad principal
```

---

## 🛠️ Tecnologías y Librerías Utilizadas

- **Lenguaje:** Kotlin
- **UI Toolkit:** Jetpack Compose & Material 3
- **Arquitectura:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Base de Datos:** Room
- **Concurrencia:** Kotlin Coroutines & Flow
- **Navegación:** Jetpack Navigation Compose

---

## 📱 Pantallas de la Aplicación

1. **Pantalla de Inicio (`PantallaInicioScreen`):** Bienvenida y punto de acceso principal a la aplicación.
2. **Catálogo de Películas (`ListaPeliculasScreen`):** Visualización en lista/tarjetas de las películas almacenadas localmente.
3. **Detalle de la Película (`DetallePeliculaScreen`):** Vista detallada que muestra información extendida (Título, Director, Género, Año de estreno y Sinopsis) consultada de forma asíncrona mediante el identificador de la película.
4. **Formulario (`FormularioPeliculaScreen`):** Pantalla destinada al registro y alta de nuevas películas en la base de datos local.

---

## ⚙️ Configuración y Ejecución

1. Clona el repositorio en tu máquina local.
2. Abre el proyecto utilizando **Android Studio** (versión Hedgehog o superior recomendada).
3. Sincroniza el proyecto con los archivos Gradle (`build.gradle.kts`).
4. Ejecuta la aplicación en un emulador con API 24 o superior (o en un dispositivo físico con depuración USB activada).

---

## 📄 Licencia

Este proyecto se distribuye bajo los términos de la licencia MIT. Consulta el archivo `LICENSE` para más detalles.