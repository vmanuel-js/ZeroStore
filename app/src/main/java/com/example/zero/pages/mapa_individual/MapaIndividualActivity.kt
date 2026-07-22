package com.example.zero.pages.mapa_individual

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.zero.components.ZeroTopBar
import com.example.zero.models.Tienda
import com.example.zero.pages.detalle_tienda.DetalleTiendaActivity
import com.example.zero.ui.theme.ZeroTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

class MapaIndividualActivity : ComponentActivity() {
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

                val posicion = LatLng (
                    tienda.latitud, tienda.longitud
                )

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        posicion,
                        17f
                    )
                }

                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        ZeroTopBar(
                            title = "Mapa de ${tienda.nombre}",
                            onBack = { finish() }
                        )
                    }
                )

                { innerPadding ->
                    DibujarMapa(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        tienda = tienda,
                        cameraPositionState = cameraPositionState,
                        onCircleClick = {
                            val intent = Intent(this, DetalleTiendaActivity::class.java)
                            intent.putExtra("tienda", tienda)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}