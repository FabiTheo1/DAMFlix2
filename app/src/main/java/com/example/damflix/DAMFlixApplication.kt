package com.example.damflix

import android.app.Application
import com.example.damflix.data.PeliculaRepository
import com.example.damflix.data.local.AppDatabase

class DAMFlixApplication : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    val repository: PeliculaRepository by lazy {
        PeliculaRepository(database.peliculaDao())
    }
}
