package com.example.zero.pages.grafico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import com.example.zero.models.Productos
import androidx.lifecycle.ViewModelProvider
import com.example.zero.components.ZeroTopBar
import com.example.zero.ui.theme.Background
import com.example.zero.ui.theme.ZeroTheme

class GraficoStockActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[GraficoStockViewModel::class.java]

        setContent {
            ZeroTheme {
                Scaffold(
                    topBar = {
                        ZeroTopBar(
                            title = "Stock por categoría",
                            onBack = { finish() }
                        )
                    },
                    containerColor = Background
                ) { innerPadding ->

                    val uiState by viewModel.uiState.collectAsState()

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val state = uiState) {
                            is GraficoStockUIState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = Color(0xFF1A1A1A)
                                )
                            }

                            is GraficoStockUIState.Error -> {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(state.message, color = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = { viewModel.fetchStock() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }

                            is GraficoStockUIState.Success -> {
                                val listaDatos   = state.datos
                                val valorMaximo  = listaDatos.maxOf { it.totalStock }.toDouble()
                                val stockBajo    = state.productosBajoStock

                                val colores = listOf(
                                    Color(0xFF1A1A1A),
                                    Color(0xFF3A3A3A),
                                    Color(0xFF555555),
                                    Color(0xFF777777),
                                    Color(0xFF999999),
                                    Color(0xFFBFA181),
                                    Color(0xFFE8540A),
                                )

                                val textMeasurer = rememberTextMeasurer()

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    item {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Stock total por categoría",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1A1A1A)
                                        )
                                    }

                                    item {
                                        Canvas(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(280.dp)
                                                .background(Background)
                                        ) {
                                            val anchoTotal      = size.width
                                            val altoTotal       = size.height
                                            val espacioEtiqueta = 55f
                                            val espacioValor    = 35f
                                            val altoGrafico     = altoTotal - espacioEtiqueta - espacioValor
                                            val anchoBarra      = (anchoTotal / listaDatos.size) * 0.55f
                                            val espacioBarra    = anchoTotal / listaDatos.size

                                            for (i in listaDatos.indices) {
                                                val color      = colores[i % colores.size]
                                                val colorRell  = color.copy(alpha = 0.3f)
                                                val colorBorde = color.copy(alpha = 1f)

                                                val proporcion = (listaDatos[i].totalStock / valorMaximo).toFloat()
                                                val altoBarra  = altoGrafico * proporcion
                                                val posX = i * espacioBarra + (espacioBarra - anchoBarra) / 2f
                                                val posY = espacioValor + (altoGrafico - altoBarra)

                                                drawRect(
                                                    color   = colorRell,
                                                    topLeft = Offset(posX, posY),
                                                    size    = Size(anchoBarra, altoBarra)
                                                )
                                                drawRect(
                                                    color   = colorBorde,
                                                    style   = Stroke(3f),
                                                    topLeft = Offset(posX, posY),
                                                    size    = Size(anchoBarra, altoBarra)
                                                )

                                                val textoValor = textMeasurer.measure(
                                                    text  = "${listaDatos[i].totalStock}",
                                                    style = TextStyle(
                                                        color      = Color(0xFF1A1A1A),
                                                        fontSize   = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                                drawText(
                                                    textLayoutResult = textoValor,
                                                    topLeft = Offset(
                                                        x = posX + anchoBarra / 2f - textoValor.size.width / 2f,
                                                        y = posY - textoValor.size.height - 4f
                                                    )
                                                )

                                                val textoEtiqueta = textMeasurer.measure(
                                                    text  = listaDatos[i].categoria,
                                                    style = TextStyle(
                                                        color    = Color(0xFF1A1A1A),
                                                        fontSize = 9.sp
                                                    )
                                                )
                                                drawText(
                                                    textLayoutResult = textoEtiqueta,
                                                    topLeft = Offset(
                                                        x = posX + anchoBarra / 2f - textoEtiqueta.size.width / 2f,
                                                        y = altoTotal - espacioEtiqueta + 8f
                                                    )
                                                )
                                            }

                                            drawLine(
                                                color       = Color(0xFF1A1A1A),
                                                start       = Offset(0f, altoTotal - espacioEtiqueta),
                                                end         = Offset(anchoTotal, altoTotal - espacioEtiqueta),
                                                strokeWidth = 2f
                                            )
                                        }
                                    }

                                    item {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Warning,
                                                contentDescription = null,
                                                tint = Color(0xFFE8540A),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                "Productos por agotarse (≤ 10 uds.)",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1A1A1A)
                                            )
                                        }
                                    }

                                    if (stockBajo.isEmpty()) {
                                        item {
                                            Text(
                                                "✅ Todos los productos tienen stock suficiente.",
                                                color = Color(0xFF555555),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    } else {
                                        items(
                                            items = stockBajo,
                                            key ={it.id_producto}
                                        ) { producto ->
                                            Card(
                                                shape  = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color(0xFFFFF3EC)
                                                ),
                                                elevation = CardDefaults.cardElevation(2.dp),
                                                modifier  = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(14.dp),
                                                    verticalAlignment     = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            producto.nombre_producto,
                                                            fontWeight = FontWeight.Bold,
                                                            style      = MaterialTheme.typography.bodyLarge,
                                                            color      = Color(0xFF1A1A1A)
                                                        )
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            producto.nombre_categoria,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = Color(0xFF777777)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Text(
                                                            "${producto.stock}",
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize   = 22.sp,
                                                            color      = if (producto.stock <= 5)
                                                                Color(0xFFD32F2F)
                                                            else
                                                                Color(0xFFE8540A)
                                                        )
                                                        Text(
                                                            "uds.",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = Color(0xFF777777)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    item { Spacer(modifier = Modifier.height(24.dp)) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}