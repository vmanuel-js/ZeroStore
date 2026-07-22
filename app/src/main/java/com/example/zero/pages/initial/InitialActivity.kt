package com.example.zero.pages.initial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import coil3.compose.AsyncImage
import com.example.zero.R
import com.example.zero.components.ZeroBottomBar
import com.example.zero.pages.carrito.CartActivity
import com.example.zero.pages.colecciones.ColeccionesActivity
import com.example.zero.pages.detalle.ProductDetailActivity
import com.example.zero.pages.favoritos.FavoritosActivity
import com.example.zero.pages.perfil.ProfileActivity
import com.example.zero.ui.theme.Background
import com.example.zero.ui.theme.BlackIcon
import com.example.zero.ui.theme.ZeroTheme
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.zero.data.ZeroDatabase
import com.example.zero.pages.chat.ChatActivity
import com.example.zero.pages.tienda.TiendaActivity
import com.example.zero.utils.API_URL

class InitialActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = ZeroDatabase.getInstance(applicationContext)

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return InitialViewModel(db.busquedaDao()) as T
            }
        }
        val viewModel = ViewModelProvider(this, factory)[InitialViewModel::class.java]

        setContent {
            ZeroTheme {
                val uiState by viewModel.uiState.collectAsState()
                val busquedasRecientes by viewModel.busquedasRecientes.collectAsState()
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            viewModel.fetchTodo()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0.dp),
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Background,
                                navigationIconContentColor = BlackIcon,
                                actionIconContentColor = BlackIcon,
                                titleContentColor = BlackIcon
                            ),
                            navigationIcon = {
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.Menu, contentDescription = "menú")
                                }
                            },
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painterResource(R.drawable.zero_black),
                                        contentDescription = "logo",
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    startActivity(Intent(this@InitialActivity, CartActivity::class.java))
                                }) {
                                    Icon(Icons.Default.ShoppingCart, contentDescription = "carrito")
                                }
                            }
                        )
                    },
                    bottomBar = {
                        ZeroBottomBar(
                            selectedIndex = 0,
                            onFavoritos = {
                                startActivity(Intent(this@InitialActivity, FavoritosActivity::class.java))
                            },
                            onTiendas = {
                                startActivity(Intent(this@InitialActivity, TiendaActivity::class.java))
                            },
                            onColecciones = {
                                startActivity(Intent(this@InitialActivity, ColeccionesActivity::class.java))
                            },
                            onPerfil = {
                                startActivity(Intent(this@InitialActivity, ProfileActivity::class.java))
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                startActivity(Intent(this@InitialActivity, ChatActivity::class.java))
                            },
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubble,
                                contentDescription = "Atención al cliente"
                            )
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (val state = uiState) {
                            is InitialUIState.Loading -> {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = Color(0xFF1A1A1A))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(stringResource(R.string.cargando), color = Color.Gray)
                                }
                            }
                            is InitialUIState.Error -> {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(state.message, color = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = { viewModel.fetchTodo() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                            is InitialUIState.Success -> {
                                var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
                                var query by remember { mutableStateOf("") }

                                val productosFiltrados = state.data.productos.filter { producto ->
                                    val coincideCategoria = categoriaSeleccionada == null || producto.nombre_categoria == categoriaSeleccionada
                                    val coincideBusqueda = query.isBlank() || producto.nombre_producto.contains(query, ignoreCase = true)
                                    coincideCategoria && coincideBusqueda
                                }

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    item {
                                        val keyboardController = LocalSoftwareKeyboardController.current

                                        OutlinedTextField(
                                            value = query,
                                            onValueChange = { query = it },
                                            placeholder = { Text(stringResource(R.string.buscar_producto), color = Color.Gray) },
                                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                            trailingIcon = {
                                                if (query.isNotBlank()) {
                                                    IconButton(onClick = {
                                                        viewModel.guardarBusqueda(query)
                                                        keyboardController?.hide()
                                                    }) {
                                                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Black)
                                                    }
                                                }
                                            },
                                            shape = RoundedCornerShape(50),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                            keyboardActions = KeyboardActions(onSearch = {
                                                viewModel.guardarBusqueda(query)
                                                keyboardController?.hide()
                                            })
                                        )

                                        if (busquedasRecientes.isNotEmpty() && query.isBlank()) {
                                            Spacer(Modifier.height(4.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Recientes", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                                TextButton(onClick = { viewModel.limpiarHistorial() }) {
                                                    Text("Limpiar", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                                                }
                                            }
                                            busquedasRecientes.forEach { busqueda ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable { query = busqueda.termino }
                                                        .padding(vertical = 6.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Default.Search, contentDescription = null,
                                                        tint = Color.Gray, modifier = Modifier.size(16.dp))
                                                    Spacer(Modifier.width(8.dp))
                                                    Text(busqueda.termino, modifier = Modifier.weight(1f),
                                                        style = MaterialTheme.typography.bodyMedium)
                                                    IconButton(
                                                        onClick = { viewModel.eliminarBusqueda(busqueda.termino) },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(Icons.Default.Close, contentDescription = "Eliminar",
                                                            tint = Color.Gray, modifier = Modifier.size(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))
                                        ) {
                                            Image(
                                                painterResource(R.drawable.descuento_imagen),
                                                contentDescription = stringResource(R.string.imagen_descuento),
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                            Column(
                                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                                verticalArrangement = Arrangement.Bottom
                                            ) {
                                                Text(stringResource(R.string.banner_titulo), color = Color.White, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.width(250.dp))
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(stringResource(R.string.descuento), color = Color.White, style = MaterialTheme.typography.displayMedium)
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                                                    Text(stringResource(R.string.banner_boton_comprar), color = Color.Black)
                                                }
                                            }
                                        }
                                    }
                                    item {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 4.dp)
                                        ) {
                                            Button(
                                                onClick = { categoriaSeleccionada = null },
                                                shape = RoundedCornerShape(50),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (categoriaSeleccionada == null) Color.Black else Color.White
                                                )
                                            ) {
                                                Text("Todos", color = if (categoriaSeleccionada == null) Color.White else Color.Black)
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            state.data.categorias.forEach { categoria ->
                                                val seleccionada = categoriaSeleccionada == categoria.nombre_categoria
                                                Button(
                                                    onClick = { categoriaSeleccionada = categoria.nombre_categoria },
                                                    shape = RoundedCornerShape(50),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (seleccionada) Color.Black else Color.White
                                                    )
                                                ) {
                                                    Text(categoria.nombre_categoria, color = if (seleccionada) Color.White else Color.Black)
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }
                                        }
                                    }
                                    items(productosFiltrados.chunked(2)) { fila ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            fila.forEach { producto ->
                                                val esFavorito = producto.id_producto in state.data.mapaFavoritos
                                                Card(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable {
                                                            val intent = Intent(context, ProductDetailActivity::class.java)
                                                            intent.putExtra("producto", producto)
                                                            intent.putExtra("imagen_url", state.data.mapaImagenes[producto.id_producto] ?: "")
                                                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                                                context, R.anim.zoom_enter, R.anim.zoom_exit
                                                            )
                                                            context.startActivity(intent, options.toBundle())
                                                        },
                                                    shape = RoundedCornerShape(16.dp),
                                                    elevation = CardDefaults.cardElevation(4.dp)
                                                ) {
                                                    Column {
                                                        Box {
                                                            AsyncImage(
                                                                model = "$API_URL${state.data.mapaImagenes[producto.id_producto]}",
                                                                contentDescription = producto.nombre_producto,
                                                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                                                contentScale = ContentScale.Crop
                                                            )
                                                            IconButton(
                                                                onClick = { viewModel.toggleFavorito(producto.id_producto) },
                                                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                                                            ) {
                                                                Icon(
                                                                    imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                                    contentDescription = "favorito",
                                                                    tint = if (esFavorito) Color.Red else Color.White
                                                                )
                                                            }
                                                        }
                                                        Column(modifier = Modifier.padding(8.dp)) {
                                                            Text(producto.nombre_producto, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                                                            Text("S/ %.2f".format(producto.precio), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                                                        }
                                                    }
                                                }
                                            }
                                            if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
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