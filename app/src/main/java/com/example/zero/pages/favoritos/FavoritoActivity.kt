package com.example.zero.pages.favoritos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil3.compose.AsyncImage
import com.example.zero.R
import com.example.zero.components.ZeroBottomBar
import com.example.zero.components.ZeroTopBar
import com.example.zero.pages.colecciones.ColeccionesActivity
import com.example.zero.pages.perfil.ProfileActivity
import com.example.zero.pages.tienda.TiendaActivity
import com.example.zero.ui.theme.ZeroTheme
import com.example.zero.utils.API_URL

class FavoritosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[FavoritosViewModel::class.java]

        setContent {
            ZeroTheme {
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    topBar = {
                        ZeroTopBar(
                            title = stringResource(R.string.title_favorito),
                            onBack = { finish() }
                        )
                    },
                    bottomBar = {
                        ZeroBottomBar(
                            selectedIndex = 1,
                            onHome = { finish() },
                            onTiendas = {
                                startActivity(Intent(this@FavoritosActivity, TiendaActivity::class.java))
                            },
                            onColecciones = {
                                startActivity(Intent(this@FavoritosActivity, ColeccionesActivity::class.java))
                            },
                            onPerfil = {
                                startActivity(Intent(this@FavoritosActivity, ProfileActivity::class.java))
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (val state = uiState) {
                            is FavoritosUIState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = Color(0xFF1A1A1A)
                                )
                            }
                            is FavoritosUIState.Error -> {
                                Column(modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(state.message, color = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = { viewModel.fetchFavoritos() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                            is FavoritosUIState.Success -> {
                                if (state.favoritos.isEmpty()) {
                                    Column(
                                        modifier = Modifier.align(Alignment.Center),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("🤍", style = MaterialTheme.typography.displayMedium)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text("Sin favoritos aún", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Agrega productos desde el detalle", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                                    }
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        item { Spacer(modifier = Modifier.height(4.dp)) }
                                        items(state.favoritos) { favorito ->
                                            Card(
                                                shape = RoundedCornerShape(16.dp),
                                                elevation = CardDefaults.cardElevation(4.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    AsyncImage(
                                                        model = "$API_URL${favorito.url_imagen ?: ""}",
                                                        contentDescription = favorito.nombre_producto,
                                                        modifier = Modifier.size(110.dp),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                    Column(
                                                        modifier = Modifier.padding(12.dp).weight(1f)
                                                    ) {
                                                        Text(favorito.nombre_categoria.uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                                        Text(favorito.nombre_producto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                                        Text("S/ %.2f".format(favorito.precio), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                                    }
                                                    IconButton(onClick = {viewModel.toggleFavorito(favorito.id_producto)}) {
                                                        Icon(
                                                            Icons.Default.Favorite,
                                                            contentDescription = "Quitar favorito",
                                                            tint = Color.Red
                                                        )
                                                    }
                                                }
                                            }
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
}