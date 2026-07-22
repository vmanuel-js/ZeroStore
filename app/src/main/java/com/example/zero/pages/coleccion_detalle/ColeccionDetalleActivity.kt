package com.example.zero.pages.coleccion_detalle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.zero.utils.API_URL
import coil3.compose.AsyncImage
import com.example.zero.components.ZeroTopBar
import com.example.zero.models.Categorias
import com.example.zero.models.ProductoCategoria
import com.example.zero.ui.theme.ZeroTheme

class ColeccionDetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val categoria = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("categoria", Categorias::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("categoria") as Categorias
        }

        val viewModel = ViewModelProvider(this)[ColeccionDetalleViewModel::class.java]
        viewModel.fetchProductos(categoria.id_categoria)

        setContent {
            ZeroTheme {
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    topBar = {
                        ZeroTopBar(
                            title = categoria.nombre_categoria,
                            onBack = { finish() }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (val state = uiState) {
                            is ColeccionDetalleUIState.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            is ColeccionDetalleUIState.Error -> {
                                Column(modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(state.message, color = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = { viewModel.fetchProductos(categoria.id_categoria) }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                            is ColeccionDetalleUIState.Success -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    item { Spacer(modifier = Modifier.height(4.dp)) }
                                    items(state.productos) { producto ->
                                        FilaProductoCategoria(producto)
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

@Composable
fun FilaProductoCategoria(producto: ProductoCategoria) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = "$API_URL${producto.url_imagen ?: ""}",
                contentDescription = producto.nombre_producto,
                modifier = Modifier.size(110.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    producto.color.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    producto.nombre_producto,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "S/ %.2f".format(producto.precio),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Stock: ${producto.stock}  •  Tallas: ${producto.talla}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}