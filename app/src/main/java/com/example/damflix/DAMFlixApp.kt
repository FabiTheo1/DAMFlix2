package com.example.damflix

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.damflix.ui.navigation.Ruta
import com.example.damflix.ui.screens.DetallePeliculaScreen
import com.example.damflix.ui.screens.FormularioPeliculaScreen
import com.example.damflix.ui.screens.ListaPeliculasScreen
import com.example.damflix.ui.screens.PantallaInicioScreen
import com.example.damflix.ui.viewmodel.PeliculaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DAMFlixApp(
    modifier: Modifier = Modifier,
    viewModel: PeliculaViewModel
) {
    val peliculas by viewModel.peliculas.collectAsState()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.drawer_header_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.drawer_header_subtitle),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                listOf(
                    Ruta.Inicio,
                    Ruta.Listado,
                    Ruta.Formulario
                ).forEach { ruta ->
                    val isSelected = rutaActual == ruta.path
                    NavigationDrawerItem(
                        label = { Text(text = ruta.label) },
                        icon = { Icon(imageVector = ruta.icon, contentDescription = ruta.label) },
                        selected = isSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (!isSelected) {
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
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when {
                                rutaActual == Ruta.Inicio.path -> stringResource(R.string.title_inicio)
                                rutaActual == Ruta.Listado.path -> stringResource(R.string.title_listado)
                                rutaActual == Ruta.Formulario.path -> stringResource(R.string.title_formulario)
                                rutaActual?.startsWith("detalle/") == true -> stringResource(R.string.detalle_titulo)
                                else -> stringResource(R.string.app_name)
                            },
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        if (rutaActual?.startsWith("detalle/") == true) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.nav_back_content_description)
                                )
                            }
                        } else {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = stringResource(R.string.nav_drawer_open)
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
                                    Text(stringResource(R.string.action_ver_guardadas, peliculas.size))
                                }
                            }
                            Ruta.Listado.path -> {
                                TextButton(onClick = { navController.navigate(Ruta.Formulario.path) }) {
                                    Text(stringResource(R.string.action_anadir_nueva))
                                }
                            }
                            Ruta.Inicio.path -> {
                                TextButton(onClick = { navController.navigate(Ruta.Listado.path) }) {
                                    Text(stringResource(R.string.action_ver_lista, peliculas.size))
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
                        onToggleFavorito = { pelicula -> viewModel.alternarFavorita(pelicula) },
                        onToggleVista = { pelicula -> viewModel.alternarVista(pelicula) },
                        onNavegarFormulario = { navController.navigate(Ruta.Formulario.path) },
                        onNavegarDetalle = { id -> navController.navigate(Ruta.Detalle.crearRuta(id)) },
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
                composable(
                    route = Ruta.Detalle.path,
                    arguments = listOf(navArgument("peliculaId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val peliculaId = backStackEntry.arguments?.getInt("peliculaId") ?: 0
                    DetallePeliculaScreen(
                        peliculaId = peliculaId,
                        viewModel = viewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
