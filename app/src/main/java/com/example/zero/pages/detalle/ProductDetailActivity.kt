package com.example.zero.pages.detalle

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.zero.R
import com.example.zero.components.ZeroTopBar
import com.example.zero.data.remote.FavoritosService
import com.example.zero.data.remote.RetrofitClient
import com.example.zero.models.Productos
import com.example.zero.ui.theme.BlackIcon
import com.example.zero.ui.theme.ZeroTheme
import kotlinx.coroutines.launch

class ProductDetailActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val producto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("producto", Productos::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("producto") as Productos
        }
        val imagenUrl = intent.getStringExtra("imagen_url") ?: ""

        onBackPressedDispatcher.addCallback(this) {
            finish()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                    OVERRIDE_TRANSITION_CLOSE,
                    R.anim.zoom_return_enter,
                    R.anim.zoom_return_exit
                )
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(R.anim.zoom_return_enter, R.anim.zoom_return_exit)
            }
        }

        enableEdgeToEdge()

        setContent {
            ZeroTheme {
                val favoritosApi = RetrofitClient.create<FavoritosService>()
                val idUsuario = com.example.zero.utils.usuarioActivo?.id_usuario ?: 0
                val scope = rememberCoroutineScope()

                var esFavorito by remember { mutableStateOf(false) }
                var cargandoFavorito by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    try {
                        val response =
                            favoritosApi.verificarFavorito(idUsuario, producto.id_producto)
                        esFavorito = response.es_favorito
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    cargandoFavorito = false
                }

                Scaffold(
                    topBar = {
                        ZeroTopBar(
                            title = producto.nombre_producto,
                            onBack = {
                                finish()
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                    overrideActivityTransition(
                                        OVERRIDE_TRANSITION_CLOSE,
                                        R.anim.zoom_return_enter,
                                        R.anim.zoom_return_exit
                                    )
                                } else {
                                    @Suppress("DEPRECATION")
                                    overridePendingTransition(R.anim.zoom_return_enter, R.anim.zoom_return_exit)
                                }
                            },
                            actions = {
                                if (!cargandoFavorito) {
                                    IconButton(onClick = {
                                        scope.launch {
                                            try {
                                                val body = mapOf(
                                                    "id_usuario" to idUsuario,
                                                    "id_producto" to producto.id_producto
                                                )
                                                if (esFavorito) {
                                                    favoritosApi.eliminarFavorito(body)
                                                } else {
                                                    favoritosApi.agregarFavorito(body)
                                                }
                                                esFavorito = !esFavorito
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "favorito",
                                            tint = if (esFavorito) Color.Red else BlackIcon
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->

                    var tallaSeleccionada by remember { mutableStateOf<String?>(null) }
                    val tallas = producto.talla.split(",")

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = "https://victorjordan105.alwaysdata.net/$imagenUrl",
                            contentDescription = producto.nombre_producto,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = producto.nombre_categoria.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = producto.nombre_producto,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "S/ ${producto.precio}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Color: ${producto.color}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Talla",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                tallas.forEach { talla ->
                                    val seleccionada = tallaSeleccionada == talla
                                    OutlinedButton(
                                        onClick = { tallaSeleccionada = talla },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (seleccionada) Color.Black else Color.Transparent,
                                            contentColor = if (seleccionada) Color.White else Color.Black
                                        ),
                                        border = BorderStroke(1.dp, Color.Black)
                                    ) {
                                        Text(talla.trim())
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.detalle_descripcion),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = producto.descripcion_producto,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.detalle_stock) + " ${producto.stock}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text(
                                    text = stringResource(R.string.detalle_boton),
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}