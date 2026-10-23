package com.example.damflix

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.damflix.ui.navigation.Ruta
import com.example.damflix.ui.screens.FormularioPeliculaScreen
import com.example.damflix.ui.screens.ListaPeliculasScreen
import com.example.damflix.ui.screens.PantallaInicioScreen
import com.example.damflix.ui.viewmodel.PeliculaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DAMFlixApp(
    modifier: Modifier = Modifier,
    viewModel: PeliculaViewModel = viewModel()
) {
    val peliculas by viewModel.peliculas.collectAsState()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    fun navegarAInicio() {
        if (rutaActual != Ruta.Inicio.path) {
            if (!navController.popBackStack(Ruta.Inicio.path, inclusive = false)) {
                navController.navigate(Ruta.Inicio.path) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                listOf(
                    Ruta.Inicio,
                    Ruta.Listado,
                    Ruta.Formulario
                ).forEach { ruta ->
                    NavigationBarItem(
                        selected = rutaActual == ruta.path,
                        onClick = {
                            if (rutaActual != ruta.path) {
                                if (ruta.path == Ruta.Inicio.path) {
                                    navegarAInicio()
                                } else {
                                    navController.navigate(ruta.path) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        },
                        icon = { Icon(ruta.icon, contentDescription = ruta.label) },
                        label = { Text(ruta.label) }
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (rutaActual) {
                            Ruta.Inicio.path -> "DAMFlix - Inicio"
                            Ruta.Listado.path -> "Lista - Mis Películas"
                            Ruta.Formulario.path -> "Alta de Película"
                            else -> "DAMFlix"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (rutaActual != Ruta.Inicio.path) {
                        IconButton(onClick = { navegarAInicio() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver a Inicio"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    when (rutaActual) {
                        Ruta.Formulario.path -> {
                            TextButton(onClick = { navController.navigate(Ruta.Listado.path) }) {
                                Text("Ver Guardadas (${peliculas.size})")
                            }
                        }
                        Ruta.Listado.path -> {
                            TextButton(onClick = { navController.navigate(Ruta.Formulario.path) }) {
                                Text("Añadir Nueva")
                            }
                        }
                        Ruta.Inicio.path -> {
                            TextButton(onClick = { navController.navigate(Ruta.Listado.path) }) {
                                Text("Ver Lista (${peliculas.size})")
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Ruta.Inicio.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Ruta.Inicio.path) {
                PantallaInicioScreen(
                    onNavegarListado = { navController.navigate(Ruta.Listado.path) },
                    onNavegarFormulario = { navController.navigate(Ruta.Formulario.path) }
                )
            }
            composable(Ruta.Listado.path) {
                ListaPeliculasScreen(
                    peliculas = peliculas,
                    onToggleFavorito = { pelicula -> viewModel.toggleFavorito(pelicula) },
                    onNavegarFormulario = { navController.navigate(Ruta.Formulario.path) },
                    onVolverInicio = { navegarAInicio() }
                )
            }
            composable(Ruta.Formulario.path) {
                FormularioPeliculaScreen(
                    onGuardarPelicula = { nuevaPelicula ->
                        viewModel.agregarPelicula(nuevaPelicula)
                    },
                    onVerListaClicked = {
                        navController.navigate(Ruta.Listado.path)
                    },
                    onVolverInicio = { navegarAInicio() }
                )
            }
        }
    }
}
