package com.example.damflix.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damflix.data.model.Pelicula

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
        Text(
            text = "Alta de Película",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título de la Película") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = director,
            onValueChange = { director = it },
            label = { Text("Director/a") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = anio,
            onValueChange = { anio = it },
            label = { Text("Año de estreno") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

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
