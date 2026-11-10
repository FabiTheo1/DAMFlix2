package com.example.damflix.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Ruta(val path: String, val label: String, val icon: ImageVector) {
    data object Inicio : Ruta("inicio", "Inicio", Icons.Filled.Home)
    data object Listado : Ruta("listado", "Listado", Icons.AutoMirrored.Filled.List)
    data object Formulario : Ruta("alta", "Añadir", Icons.Filled.Add)
    data object Detalle : Ruta("detalle/{peliculaId}", "Detalle", Icons.Filled.Info) {
        fun crearRuta(peliculaId: Int): String = "detalle/$peliculaId"
    }
}
