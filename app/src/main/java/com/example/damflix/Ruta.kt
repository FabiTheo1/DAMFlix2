package com.example.damflix

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Ruta(val path: String, val label: String, val icon: ImageVector) {
    object Inicio: Ruta("inicio", "Inicio", Icons.Filled.Home)
    object Listado: Ruta("listado", "Listado", Icons.AutoMirrored.Filled.List)
    object Formulario: Ruta("alta", "Añadir", Icons.Filled.Add)
}