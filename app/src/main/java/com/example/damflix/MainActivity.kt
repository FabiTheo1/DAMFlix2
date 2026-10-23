package com.example.damflix

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damflix.ui.theme.DAMFlixTheme

// Modelo de datos para las películas
data class Pelicula(
    val titulo: String,
    val director: String,
    val anio: String,
    val esVista: Boolean,
    val esFavorita: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DAMFlixTheme {
                DAMFlixApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DAMFlixApp(modifier: Modifier = Modifier) {
    // Lista persistente en memoria durante la ejecución
    val peliculas = remember { mutableStateListOf<Pelicula>() }
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
                    onToggleFavorito = { peliculaAModificar ->
                        val index = peliculas.indexOf(peliculaAModificar)
                        if (index != -1) {
                            peliculas[index] = peliculaAModificar.copy(
                                esFavorita = !peliculaAModificar.esFavorita
                            )
                        }
                    },
                    onNavegarFormulario = { navController.navigate(Ruta.Formulario.path) },
                    onVolverInicio = { navegarAInicio() }
                )
            }
            composable(Ruta.Formulario.path) {
                FormularioPeliculaScreen(
                    onGuardarPelicula = { nuevaPelicula ->
                        peliculas.add(nuevaPelicula)
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

@Composable
fun PantallaInicioScreen(
    modifier: Modifier = Modifier,
    onNavegarListado: () -> Unit = {},
    onNavegarFormulario: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono o logotipo principal
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = "DAMFlix Logo",
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título de bienvenida
        Text(
            text = "Bienvenido/a a DAMFlix",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Descripción de la aplicación
        Text(
            text = "Tu catálogo personal de películas. Gestiona tus títulos guardados y tus favoritos en un solo lugar.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tarjeta informativa o botones de acceso directo
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Acciones Rápidas 🚀",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Button(
                    onClick = onNavegarListado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Catálogo de Películas")
                }
                OutlinedButton(
                    onClick = onNavegarFormulario,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir Nueva Película")
                }
            }
        }
    }
}

@Composable
fun FormularioPeliculaScreen(
    modifier: Modifier = Modifier,
    onGuardarPelicula: (Pelicula) -> Unit = {},
    onVerListaClicked: () -> Unit = {},
    onVolverInicio: () -> Unit = {}
) {
    val context = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var esVista by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Título de la pantalla
        Text(
            text = "Alta de Película",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Campo 1: Título de la película
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título de la Película") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo 2: Director/a
        OutlinedTextField(
            value = director,
            onValueChange = { director = it },
            label = { Text("Director/a") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo 3: Año de estreno (Teclado numérico)
        OutlinedTextField(
            value = anio,
            onValueChange = { anio = it },
            label = { Text("Año de estreno") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Checkbox: Película vista o pendiente
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = esVista,
                onCheckedChange = { esVista = it }
            )
            Text(
                text = if (esVista) "Ya he visto esta película" else "Película pendiente de ver",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botones de acción inferior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onVolverInicio,
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver a Inicio")
            }
            OutlinedButton(
                onClick = onVerListaClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text("Ver Películas")
            }
        }

        // Botón de guardado con aviso Toast si algún campo está vacío
        Button(
            onClick = {
                if (titulo.isBlank() || director.isBlank() || anio.isBlank()) {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    val pelicula = Pelicula(
                        titulo = titulo.trim(),
                        director = director.trim(),
                        anio = anio.trim(),
                        esVista = esVista
                    )
                    onGuardarPelicula(pelicula)
                    Toast.makeText(context, "Película guardada", Toast.LENGTH_SHORT).show()
                    // Limpiar campos tras guardar
                    titulo = ""
                    director = ""
                    anio = ""
                    esVista = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Película")
        }
    }
}

@Composable
fun ListaPeliculasScreen(
    peliculas: List<Pelicula>,
    modifier: Modifier = Modifier,
    onToggleFavorito: (Pelicula) -> Unit = {},
    onNavegarFormulario: () -> Unit = {},
    onVolverInicio: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Películas Guardadas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (peliculas.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay películas guardadas.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = peliculas, key = { pelicula -> pelicula.titulo })
                { pelicula ->
                    PeliculaItemCard(
                        pelicula = pelicula,
                        onToggleFavorito = { onToggleFavorito(pelicula) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onVolverInicio,
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver a Inicio")
            }
            Button(
                onClick = onNavegarFormulario,
                modifier = Modifier.weight(1f)
            ) {
                Text("Añadir Película")
            }
        }
    }
}

@Composable
fun PeliculaItemCard(
    pelicula: Pelicula,
    onToggleFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = pelicula.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Director/a: ${pelicula.director}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Año: ${pelicula.anio}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (pelicula.esVista) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = if (pelicula.esVista) "Vista" else "Pendiente",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (pelicula.esVista) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            IconButton(
                onClick = onToggleFavorito
            ) {
                Icon(
                    imageVector = if (pelicula.esFavorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Añadir a favoritos",
                    tint = if (pelicula.esFavorita) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormularioPeliculaPreview() {
    DAMFlixTheme {
        FormularioPeliculaScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ListaPeliculasPreview() {
    DAMFlixTheme {
        ListaPeliculasScreen(
            peliculas = listOf(
                Pelicula("El Padrino", "Francis Ford Coppola", "1972", true),
                Pelicula("Inception", "Christopher Nolan", "2010", false)
            )
        )
    }
}