package com.example.damflix.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PeliculaDao {

    @Query("SELECT * FROM peliculas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Pelicula>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(pelicula: Pelicula)

    @Update
    suspend fun actualizar(pelicula: Pelicula)

    @Delete
    suspend fun eliminar(pelicula: Pelicula)
}
