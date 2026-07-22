package com.example.zero.pages.tienda

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.zero.R
import com.example.zero.components.ZeroTopBar
import com.example.zero.pages.mapa_general.MapaGeneralActivity
import com.example.zero.pages.mapa_individual.MapaIndividualActivity
import com.example.zero.ui.theme.ZeroTheme

class TiendaActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[TiendaViewModel::class.java]

        setContent {
            ZeroTheme {
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        ZeroTopBar(
                            title = stringResource(R.string.title_tiendas),
                            onBack = { finish() }
                        )
                    }
                )
                { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        when (val state = uiState) {

                            is TiendaUIState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                            is TiendaUIState.Error -> {
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

                            is TiendaUIState.Success -> {
                                Column {
                                    LazyColumn(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(16.dp)
                                    ) {
                                        items(state.tiendas) { tienda ->
                                            FilaTienda(
                                                tienda = tienda,
                                                modifier = Modifier.clickable {
                                                    val intent = Intent(
                                                        this@TiendaActivity,
                                                        MapaIndividualActivity::class.java
                                                    )
                                                    intent.putExtra("tienda", tienda)
                                                    startActivity(intent)
                                                }
                                            )
                                        }
                                    }

                                    Button(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        onClick = {
                                            startActivity(
                                                Intent (
                                                    this@TiendaActivity,
                                                    MapaGeneralActivity::class.java
                                                )
                                            )
                                        }
                                    ) {
                                        Text("Ver Todos")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}