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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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

// Enum para la navegación entre pantallas
enum class Pantalla {
    FORMULARIO,
    LISTA
}

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
    var pantallaActual by remember { mutableStateOf(Pantalla.FORMULARIO) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (pantallaActual == Pantalla.FORMULARIO) "Lista de Películas" else "Lista - Mis Películas",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    if (pantallaActual == Pantalla.FORMULARIO) {
                        TextButton(onClick = { pantallaActual = Pantalla.LISTA }) {
                            Text("Ver Guardadas (${peliculas.size})")
                        }
                    } else {
                        TextButton(onClick = { pantallaActual = Pantalla.FORMULARIO }) {
                            Text("Añadir Nueva")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when (pantallaActual) {
            Pantalla.FORMULARIO -> {
                FormularioPeliculaScreen(
                    modifier = Modifier.padding(innerPadding),
                    onGuardarPelicula = { nuevaPelicula ->
                        peliculas.add(nuevaPelicula)
                    },
                    onVerListaClicked = {
                        pantallaActual = Pantalla.LISTA
                    }
                )
            }
            Pantalla.LISTA -> {
                ListaPeliculasScreen(
                    peliculas = peliculas,
                    modifier = Modifier.padding(innerPadding),
                    onNavegarFormulario = {
                        pantallaActual = Pantalla.FORMULARIO
                    }
                )
            }
        }
    }
}

@Composable
fun FormularioPeliculaScreen(
    modifier: Modifier = Modifier,
    onGuardarPelicula: (Pelicula) -> Unit = {},
    onVerListaClicked: () -> Unit = {}
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

        // Botón para navegar a la pantalla de lista
        OutlinedButton(
            onClick = onVerListaClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Películas Guardadas")
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
    onNavegarFormulario: () -> Unit = {}
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

        Button(
            onClick = onNavegarFormulario,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir otra película")
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