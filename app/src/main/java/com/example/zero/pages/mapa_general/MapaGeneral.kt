package com.example.zero.pages.mapa_general

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.zero.models.Tienda
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapaGeneral(
    modifier: Modifier = Modifier,
    tiendas: List<Tienda>,
    onCircleClick: (Tienda) -> Unit
) {

    if (tiendas.isEmpty()) return

    val posicionInicial = LatLng(
        tiendas.first().latitud,
        tiendas.first().longitud
    )

    val cameraPositionState = rememberCameraPositionState {

        position = CameraPosition.fromLatLngZoom(
            posicionInicial,
            12f
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {

        tiendas.forEach { tienda ->

            val posicion = LatLng(
                tienda.latitud,
                tienda.longitud
            )

            Marker(
                state = MarkerState(position = posicion),
                title = tienda.nombre,
                snippet = tienda.distrito
            )

            Circle(
                center = posicion,
                radius = 200.0,
                fillColor = Color(0x330066FF),
                strokeColor = Color.Blue,
                clickable = true,
                onClick = {
                    onCircleClick(tienda)
                }
            )
        }
    }
}