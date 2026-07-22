package com.example.zero.pages.colecciones

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.zero.components.ZeroBottomBar
import com.example.zero.components.ZeroTopBar
import com.example.zero.models.Categorias
import com.example.zero.pages.carrito.CartActivity
import com.example.zero.pages.coleccion_detalle.ColeccionDetalleActivity
import com.example.zero.pages.favoritos.FavoritosActivity
import com.example.zero.pages.perfil.ProfileActivity
import com.example.zero.pages.tienda.TiendaActivity
import com.example.zero.ui.theme.ZeroTheme

class ColeccionesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[ColeccionesViewModel::class.java]

        setContent {
            ZeroTheme {
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    topBar = {
                        ZeroTopBar(
                            title = "Colecciones",
                            onBack = { finish() }
                        )
                    },
                    bottomBar = {
                        ZeroBottomBar(
                            selectedIndex = 3,
                            onHome = { finish() },
                            onFavoritos = {
                                startActivity(Intent(this@ColeccionesActivity, FavoritosActivity::class.java))
                            },
                            onTiendas = {
                                startActivity(Intent(this@ColeccionesActivity, TiendaActivity::class.java))
                            },
                            onPerfil = {
                                startActivity(Intent(this@ColeccionesActivity, ProfileActivity::class.java))
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (val state = uiState) {
                            is ColeccionesUIState.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            is ColeccionesUIState.Error -> {
                                Column(modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(state.message, color = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = { viewModel.fetchCategorias() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                            is ColeccionesUIState.Success -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    item { Spacer(modifier = Modifier.height(4.dp)) }
                                    items(state.categorias) { categoria ->
                                        FilaCategoria(
                                            categoria = categoria,
                                            modifier = Modifier.clickable {
                                                val intent = Intent(
                                                    this@ColeccionesActivity,
                                                    ColeccionDetalleActivity::class.java
                                                )
                                                intent.putExtra("categoria", categoria)
                                                startActivity(intent)
                                            }
                                        )
                                    }
                                    item { Spacer(modifier = Modifier.height(16.dp)) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}