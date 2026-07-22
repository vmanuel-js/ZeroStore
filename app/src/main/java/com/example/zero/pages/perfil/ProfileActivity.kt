package com.example.zero.pages.perfil

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.zero.pages.carrito.CartActivity
import com.example.zero.pages.favoritos.FavoritosActivity
import com.example.zero.R
import com.example.zero.components.ZeroBottomBar
import com.example.zero.components.ZeroTopBar
import com.example.zero.data.local.UserStore
import com.example.zero.pages.admin.AdminProductosActivity
import com.example.zero.pages.colecciones.ColeccionesActivity
import com.example.zero.pages.grafico.GraficoStockActivity
import com.example.zero.pages.login.LoginActivity
import com.example.zero.pages.tienda.TiendaActivity
import com.example.zero.ui.theme.ZeroTheme
import com.example.zero.utils.usuarioActivo
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ZeroTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets(0.dp),
                    topBar = {
                        ZeroTopBar(
                            title = stringResource(R.string.title_perfil),
                            onBack = { finish() }
                        )
                    },
                    bottomBar = {
                        ZeroBottomBar(
                            selectedIndex = 4,
                            onHome = { finish() },
                            onFavoritos = {
                                startActivity(Intent(this@ProfileActivity, FavoritosActivity::class.java))
                            },
                            onTiendas = {
                                startActivity(Intent(this@ProfileActivity, TiendaActivity::class.java))
                            },
                            onColecciones = {
                                startActivity(Intent(this@ProfileActivity, ColeccionesActivity::class.java))
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Companion.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.Companion.height(24.dp))

                        Surface(
                            modifier = Modifier.Companion.size(100.dp),
                            shape = CircleShape,
                            color = Color(0xFF1A1A1A)
                        ) {
                            Box(contentAlignment = Alignment.Companion.Center) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.Companion.White,
                                    modifier = Modifier.Companion.size(52.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.Companion.height(16.dp))

                        Text(
                            usuarioActivo?.nombre_usuario ?: "Usuario",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            usuarioActivo?.email_usuario ?: "",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.Companion.height(32.dp))

                        if (usuarioActivo?.rol == "admin") {
                            OutlinedButton(
                                onClick = {
                                    startActivity(Intent(this@ProfileActivity, AdminProductosActivity::class.java))
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text("Administrar productos", modifier = Modifier.align(Alignment.CenterStart))
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    startActivity(Intent(this@ProfileActivity, GraficoStockActivity::class.java))
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text("Stock por categoría", modifier = Modifier.align(Alignment.CenterStart))
                                }
                            }
                        }

                        listOf(
                            "Mis pedidos",
                            "Direcciones guardadas",
                            "Métodos de pago",
                            "Notificaciones",
                            "Cerrar sesión"
                        ).forEach { opcion ->
                            OutlinedButton(
                                onClick = {
                                    when (opcion) {
                                        "Administrar productos" -> {
                                            startActivity(Intent(this@ProfileActivity, AdminProductosActivity::class.java))
                                        }
                                        "Cerrar sesión" -> {
                                            usuarioActivo = null
                                            val userStore = UserStore(this@ProfileActivity)
                                            lifecycleScope.launch {
                                                userStore.guardarDatosUsuario("")
                                            }
                                            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            })
                                        }
                                    }
                                },
                                modifier = Modifier.Companion.fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(
                                        0xFF1A1A1A
                                    )
                                )
                            ) {
                                Box(modifier = Modifier.Companion.fillMaxWidth()) {
                                    Text(
                                        opcion,
                                        modifier = Modifier.Companion.align(Alignment.Companion.CenterStart)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}