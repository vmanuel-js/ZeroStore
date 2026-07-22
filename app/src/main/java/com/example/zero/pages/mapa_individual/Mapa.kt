package com.example.zero.pages.mapa_individual

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.zero.models.Tienda
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun DibujarMapa (
    modifier: Modifier = Modifier,
    tienda: Tienda,
    cameraPositionState: CameraPositionState,
    onCircleClick: () -> Unit
) {
    val posicion = LatLng (
        tienda.latitud,
        tienda.longitud
    )

    GoogleMap (
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        Marker (
            state = MarkerState(position = posicion),
            title = tienda.nombre,
            snippet = tienda.distrito
        )

        Circle (
            center = posicion,
            radius = 200.0,
            fillColor = Color(0x330066FF),
            strokeColor = Color.Blue,
            clickable = true,
            onClick = {
                onCircleClick()
            }
        )
    }
}