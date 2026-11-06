package com.example.damflix.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damflix.data.PeliculaRepository
import com.example.damflix.data.local.PeliculaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PeliculaViewModel(private val repository: PeliculaRepository) : ViewModel() {

    val peliculas: StateFlow<List<PeliculaEntity>> = repository.peliculas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun agregarPelicula(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            repository.insertar(pelicula)
        }
    }

    fun alternarVista(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            repository.toggleVista(pelicula)
        }
    }

    fun alternarFavorita(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            repository.toggleFavorito(pelicula)
        }
    }

    fun eliminarPelicula(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            repository.eliminar(pelicula)
        }
    }
}
