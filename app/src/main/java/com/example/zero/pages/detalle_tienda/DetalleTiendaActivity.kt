package com.example.zero.pages.detalle_tienda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.zero.R
import com.example.zero.components.ZeroTopBar
import com.example.zero.models.Tienda
import com.example.zero.ui.theme.ZeroTheme
import com.example.zero.utils.API_URL

class DetalleTiendaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tienda = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("tienda", Tienda::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("tienda") as Tienda
        }

        setContent {
            ZeroTheme {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        ZeroTopBar(
                            title = "Detalle de ${tienda.nombre}",
                            onBack = { finish() }
                        )
                    }
                ) { innerPadding ->
                    DetalleTiendaScreen (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        tienda = tienda
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleTiendaScreen(
    modifier: Modifier = Modifier,
    tienda: Tienda
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "$API_URL${tienda.imagen_url}",
            contentDescription = tienda.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = tienda.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Distrito: ${tienda.distrito}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Descripción: ${tienda.descripcion}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Teléfono: ${tienda.telefono ?: "No disponible"}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Horario: ${tienda.horario ?: "No disponible"}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Latitud: ${tienda.latitud}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Longitud: ${tienda.longitud}")
            }
        }
    }
}