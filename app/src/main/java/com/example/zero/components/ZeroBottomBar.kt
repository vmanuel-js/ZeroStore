package com.example.zero.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.zero.R

@Composable
fun ZeroBottomBar(
    selectedIndex: Int = 0,
    onHome: () -> Unit = {},
    onFavoritos: () -> Unit = {},
    onTiendas: () -> Unit = {},
    onColecciones: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    val items = listOf(
        Triple(Icons.Default.Home, stringResource(R.string.home_icon), onHome),
        Triple(Icons.Default.FavoriteBorder, stringResource(R.string.favoritos_icon), onFavoritos),
        Triple(Icons.Default.ShoppingCart, stringResource(R.string.tiendas_icon), onTiendas),
        Triple(Icons.Default.KeyboardArrowUp, "Colecciones", onColecciones),
        Triple(Icons.Default.Person, stringResource(R.string.perfil_icon), onPerfil)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFF1A1A1A),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, (icon, label, onClick) ->
                    val selected = selectedIndex == index
                    IconButton(
                        onClick = onClick,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .size(48.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = if (selected) Color(0xFFE8540A) else Color.Transparent,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}