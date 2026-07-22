package com.example.zero.pages.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.zero.components.ZeroTopBar
import com.example.zero.ui.theme.ZeroTheme

class AdminProductosActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[AdminProductosViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ZeroTheme {

                val uiState by viewModel.uiState.collectAsState()
                var mostrarBottomSheet by remember { mutableStateOf(false) }
                var mostrarActualizar by remember { mutableStateOf(false) }
                var mostrarConfirmarEliminar by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        ZeroTopBar(title= "Gestión de productos", onBack = {finish()})
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                viewModel.limpiarFormulario()
                                mostrarActualizar = false
                                mostrarBottomSheet = true
                            },
                            containerColor =  Color.Black
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = Color.White)
                        }
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (val state = uiState) {
                                is AdminProductosUIState.Loading -> {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }

                                is AdminProductosUIState.Error -> {
                                    Column(
                                        modifier = Modifier.align(Alignment.Center),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(state.message, color = MaterialTheme.colorScheme.error)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(onClick = { viewModel.fetchProductos() }) {
                                            Text("Reintentar")
                                        }
                                    }
                                }

                                is AdminProductosUIState.Success -> {
                                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                                        items(state.productos, key = { it.id_producto }) { producto ->
                                            FilaProductoAdmin (
                                                producto = producto,
                                                onEditar = {
                                                    viewModel.idProducto  = producto.id_producto.toString()
                                                    viewModel.nombre      = producto.nombre_producto
                                                    viewModel.descripcion = producto.descripcion_producto
                                                    viewModel.precio      = producto.precio.toString()
                                                    viewModel.stock       = producto.stock.toString()
                                                    viewModel.talla       = producto.talla
                                                    viewModel.color       = producto.color
                                                    viewModel.idCategoria = producto.id_categoria.toString()
                                                    mostrarActualizar  = true
                                                    mostrarBottomSheet = true
                                                },
                                                onEliminar = {
                                                    viewModel.idProducto = producto.id_producto.toString()
                                                    mostrarConfirmarEliminar = true
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (mostrarBottomSheet) {
                        ModalBottomSheet(onDismissRequest = { mostrarBottomSheet = false }) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(bottom = 32.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = if (mostrarActualizar) "Editar producto #${viewModel.idProducto}"
                                    else "Nuevo producto",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                OutlinedTextField(
                                    label = { Text("Nombre") },
                                    value = viewModel.nombre,
                                    onValueChange = { viewModel.nombre = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    label = { Text("Descripción") },
                                    value = viewModel.descripcion,
                                    onValueChange = { viewModel.descripcion = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    label = { Text("Precio (ej: 29.90)") },
                                    value = viewModel.precio,
                                    onValueChange = { viewModel.precio = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    label = { Text("Stock") },
                                    value = viewModel.stock,
                                    onValueChange = { viewModel.stock = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    label = { Text("Talla (ej: S,M,L,XL)") },
                                    value = viewModel.talla,
                                    onValueChange = { viewModel.talla = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    label = { Text("Color") },
                                    value = viewModel.color,
                                    onValueChange = { viewModel.color = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    label = { Text("ID Categoría") },
                                    value = viewModel.idCategoria,
                                    onValueChange = { viewModel.idCategoria = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Button(
                                    onClick = {
                                        mostrarBottomSheet = false
                                        if (mostrarActualizar) viewModel.actualizarProducto()
                                        else viewModel.insertarProducto()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                ) {
                                    Text(
                                        if (mostrarActualizar) "Actualizar" else "Guardar",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    if (mostrarConfirmarEliminar) {
                        AlertDialog(
                            onDismissRequest = { mostrarConfirmarEliminar = false },
                            icon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            },
                            title = { Text("Eliminar producto") },
                            text  = { Text("¿Estás seguro que deseas eliminar el producto #${viewModel.idProducto}? Esta acción lo desactivará de la tienda.") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        mostrarConfirmarEliminar = false
                                        viewModel.eliminarProducto()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) { Text("Eliminar", color = Color.White) }
                            },
                            dismissButton = {
                                OutlinedButton(onClick = { mostrarConfirmarEliminar = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}