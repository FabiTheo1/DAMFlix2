package com.example.damflix.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.damflix.data.PeliculaRepository
import com.example.damflix.data.model.Pelicula
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PeliculaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PeliculaRepository(application)

    private val _peliculas = MutableStateFlow<List<Pelicula>>(emptyList())
    val peliculas: StateFlow<List<Pelicula>> = _peliculas.asStateFlow()

    init {
        cargarPeliculas()
    }

    fun cargarPeliculas() {
        viewModelScope.launch {
            _peliculas.value = repository.obtenerTodas()
        }
    }

    fun agregarPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            repository.insertar(pelicula)
            cargarPeliculas()
        }
    }

    fun toggleFavorito(pelicula: Pelicula) {
        viewModelScope.launch {
            repository.actualizarFavorito(pelicula)
            cargarPeliculas()
        }
    }
}
