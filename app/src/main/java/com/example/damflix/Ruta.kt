package com.example.damflix

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.*

sealed class Ruta(val path: String, val label: String, val icon: ImageVector) {
    object Inicio: Ruta("inicio", "Inicio", Icons.Filled.Home)
    object Listado: Ruta("listado", "Listado", Icons.Filled.List)
    object Formulario: Ruta("alta", "Añadir", Icons.Filled.Add)
}