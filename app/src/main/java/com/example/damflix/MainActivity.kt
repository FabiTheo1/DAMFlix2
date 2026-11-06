package com.example.damflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.damflix.ui.theme.DAMFlixTheme
import com.example.damflix.ui.viewmodel.PeliculaViewModel
import com.example.damflix.ui.viewmodel.PeliculaViewModelFactory

class MainActivity : ComponentActivity() {

    private val peliculaViewModel: PeliculaViewModel by viewModels {
        val app = application as DAMFlixApplication
        PeliculaViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DAMFlixTheme {
                DAMFlixApp(viewModel = peliculaViewModel)
            }
        }
    }
}
