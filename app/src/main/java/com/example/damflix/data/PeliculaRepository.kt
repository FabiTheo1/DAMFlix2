package com.example.damflix.data

import android.content.ContentValues
import android.content.Context
import com.example.damflix.data.model.Pelicula
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PeliculaRepository(context: Context) {
    private val dbHelper = DAMFlixDbHelper(context.applicationContext)

    suspend fun obtenerTodas(): List<Pelicula> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Pelicula>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DAMFlixDbHelper.TABLE_PELICULAS,
            null,
            null,
            null,
            null,
            null,
            "${DAMFlixDbHelper.COLUMN_ID} DESC"
        )

        cursor.use { c ->
            val idIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_ID)
            val tituloIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_TITULO)
            val directorIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_DIRECTOR)
            val generoIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_GENERO)
            val anioIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_ANIO)
            val sinopsisIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_SINOPSIS)
            val esVistaIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_ES_VISTA)
            val esFavoritaIndex = c.getColumnIndexOrThrow(DAMFlixDbHelper.COLUMN_ES_FAVORITA)

            while (c.moveToNext()) {
                val pelicula = Pelicula(
                    id = c.getLong(idIndex),
                    titulo = c.getString(tituloIndex),
                    director = c.getString(directorIndex),
                    genero = c.getString(generoIndex) ?: "",
                    anio = c.getString(anioIndex),
                    sinopsis = c.getString(sinopsisIndex) ?: "",
                    esVista = c.getInt(esVistaIndex) == 1,
                    esFavorita = c.getInt(esFavoritaIndex) == 1
                )
                lista.add(pelicula)
            }
        }
        lista
    }

    suspend fun insertar(pelicula: Pelicula): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DAMFlixDbHelper.COLUMN_TITULO, pelicula.titulo)
            put(DAMFlixDbHelper.COLUMN_DIRECTOR, pelicula.director)
            put(DAMFlixDbHelper.COLUMN_GENERO, pelicula.genero)
            put(DAMFlixDbHelper.COLUMN_ANIO, pelicula.anio)
            put(DAMFlixDbHelper.COLUMN_SINOPSIS, pelicula.sinopsis)
            put(DAMFlixDbHelper.COLUMN_ES_VISTA, if (pelicula.esVista) 1 else 0)
            put(DAMFlixDbHelper.COLUMN_ES_FAVORITA, if (pelicula.esFavorita) 1 else 0)
        }
        db.insert(DAMFlixDbHelper.TABLE_PELICULAS, null, values)
    }

    suspend fun actualizarFavorito(pelicula: Pelicula): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val nuevoEstadoFavorito = !pelicula.esFavorita
        val values = ContentValues().apply {
            put(DAMFlixDbHelper.COLUMN_ES_FAVORITA, if (nuevoEstadoFavorito) 1 else 0)
        }
        db.update(
            DAMFlixDbHelper.TABLE_PELICULAS,
            values,
            "${DAMFlixDbHelper.COLUMN_ID} = ?",
            arrayOf(pelicula.id.toString())
        )
    }
}
