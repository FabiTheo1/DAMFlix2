package com.example.damflix.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damflix.data.PeliculaRepository
import com.example.damflix.data.local.Pelicula
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PeliculaViewModel(private val repository: PeliculaRepository) : ViewModel() {

    val peliculas: StateFlow<List<Pelicula>> = repository.peliculas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun agregarPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            repository.insertar(pelicula)
        }
    }

    fun toggleFavorito(pelicula: Pelicula) {
        viewModelScope.launch {
            repository.toggleFavorito(pelicula)
        }
    }
}
