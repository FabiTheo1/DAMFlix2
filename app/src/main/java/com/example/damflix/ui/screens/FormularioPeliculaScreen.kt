package com.example.damflix.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damflix.R
import com.example.damflix.data.local.PeliculaEntity

private fun contarPalabras(texto: String): Int {
    val trimmed = texto.trim()
    if (trimmed.isEmpty()) return 0
    return trimmed.split("\\s+".toRegex()).size
}

@Composable
fun FormularioPeliculaScreen(
    modifier: Modifier = Modifier,
    onGuardarPelicula: (PeliculaEntity) -> Unit = {},
    onVerListaClicked: () -> Unit = {},
    onVolverInicio: () -> Unit = {}
) {
    val context = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }
    var esVista by remember { mutableStateOf(false) }

    val numPalabrasSinopsis = contarPalabras(sinopsis)

    val fillFieldsMsg = stringResource(R.string.toast_fill_all_fields)
    val savedMsg = stringResource(R.string.toast_movie_saved)
    val limitExceededMsg = stringResource(R.string.toast_synopsis_limit_exceeded)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.form_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text(stringResource(R.string.form_label_titulo)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = director,
            onValueChange = { director = it },
            label = { Text(stringResource(R.string.form_label_director)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text(stringResource(R.string.form_label_genero)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = anio,
            onValueChange = { anio = it },
            label = { Text(stringResource(R.string.form_label_anio)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = sinopsis,
            onValueChange = { nuevoTexto ->
                val palabrasNuevas = contarPalabras(nuevoTexto)
                if ((palabrasNuevas <= 150) || (nuevoTexto.length < sinopsis.length)) {
                    sinopsis = nuevoTexto
                }
            },
            label = { Text(stringResource(R.string.form_label_sinopsis)) },
            supportingText = {
                Text(
                    text = stringResource(R.string.synopsis_word_count, numPalabrasSinopsis),
                    color = if (numPalabrasSinopsis > 150) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            isError = numPalabrasSinopsis > 150,
            singleLine = false,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
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
                text = if (esVista) stringResource(R.string.form_checkbox_vista) else stringResource(R.string.form_checkbox_pendiente),
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
                Text(stringResource(R.string.btn_volver_inicio))
            }
            OutlinedButton(
                onClick = onVerListaClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.btn_ver_peliculas))
            }
        }

        Button(
            onClick = {
                if (titulo.isBlank() || director.isBlank() || anio.isBlank()) {
                    Toast.makeText(context, fillFieldsMsg, Toast.LENGTH_SHORT).show()
                } else if (numPalabrasSinopsis > 150) {
                    Toast.makeText(context, limitExceededMsg, Toast.LENGTH_SHORT).show()
                } else {
                    val pelicula = PeliculaEntity(
                        titulo = titulo.trim(),
                        director = director.trim(),
                        genero = genero.trim(),
                        anio = anio.trim(),
                        sinopsis = sinopsis.trim(),
                        esVista = esVista
                    )
                    onGuardarPelicula(pelicula)
                    Toast.makeText(context, savedMsg, Toast.LENGTH_SHORT).show()
                    titulo = ""
                    director = ""
                    genero = ""
                    anio = ""
                    sinopsis = ""
                    esVista = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_guardar_pelicula))
        }
    }
}
