package com.example.damflix.data

import com.example.damflix.data.local.PeliculaDao
import com.example.damflix.data.local.PeliculaEntity
import kotlinx.coroutines.flow.Flow

class PeliculaRepository(private val peliculaDao: PeliculaDao) {

    val peliculas: Flow<List<PeliculaEntity>> = peliculaDao.obtenerTodas()

    suspend fun obtenerPeliculaPorId(id: Int): PeliculaEntity? = peliculaDao.obtenerPorId(id)

    suspend fun insertar(pelicula: PeliculaEntity) {
        peliculaDao.insertar(pelicula)
    }

    suspend fun actualizar(pelicula: PeliculaEntity) {
        peliculaDao.actualizar(pelicula)
    }

    suspend fun toggleFavorito(pelicula: PeliculaEntity) {
        val actualizada = pelicula.copy(esFavorita = !pelicula.esFavorita)
        peliculaDao.actualizar(actualizada)
    }

    suspend fun toggleVista(pelicula: PeliculaEntity) {
        val actualizada = pelicula.copy(esVista = !pelicula.esVista)
        peliculaDao.actualizar(actualizada)
    }

    suspend fun eliminar(pelicula: PeliculaEntity) {
        peliculaDao.eliminar(pelicula)
    }
}
