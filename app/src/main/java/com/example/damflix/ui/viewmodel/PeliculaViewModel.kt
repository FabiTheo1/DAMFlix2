package com.example.damflix.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.damflix.data.model.Pelicula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PeliculaViewModel : ViewModel() {
    private val _peliculas = MutableStateFlow<List<Pelicula>>(emptyList())
    val peliculas: StateFlow<List<Pelicula>> = _peliculas.asStateFlow()

    fun agregarPelicula(pelicula: Pelicula) {
        _peliculas.update { actual ->
            actual + pelicula
        }
    }

    fun toggleFavorito(pelicula: Pelicula) {
        _peliculas.update { actual ->
            actual.map {
                if (it.titulo == pelicula.titulo) {
                    it.copy(esFavorita = !it.esFavorita)
                } else {
                    it
                }
            }
        }
    }
}
