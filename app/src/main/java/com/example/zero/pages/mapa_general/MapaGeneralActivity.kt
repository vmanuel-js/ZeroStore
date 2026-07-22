package com.example.zero.pages.mapa_general

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.zero.components.ZeroTopBar
import com.example.zero.pages.detalle_tienda.DetalleTiendaActivity
import com.example.zero.ui.theme.ZeroTheme

class MapaGeneralActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[MapaGeneralViewModel::class.java]

        setContent {
            ZeroTheme {

                val uiState by viewModel.uiState.collectAsState()

                Scaffold (
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        ZeroTopBar(
                            title = "Mapa General de las tiendas Zero",
                            onBack = { finish() }
                        )
                    }
                )
                { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val state = uiState) {

                            is MapaGeneralUIState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                            is MapaGeneralUIState.Error -> {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(state.message, color = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = { viewModel.fetchTiendas() }) {
                                        Text("Reintentar")
                                    }
                                }
                            }

                            is MapaGeneralUIState.Success -> {
                                MapaGeneral (
                                    modifier = Modifier.fillMaxSize(),
                                    tiendas = state.tiendas,
                                    onCircleClick = { tienda ->
                                        val intent = Intent (
                                            this@MapaGeneralActivity, DetalleTiendaActivity::class.java
                                        )
                                        intent.putExtra(
                                            "tienda", tienda
                                        )
                                        startActivity(intent)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}