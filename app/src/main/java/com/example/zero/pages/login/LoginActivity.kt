package com.example.zero.pages.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.zero.MainActivity
import com.example.zero.data.local.UserStore
import com.example.zero.models.LoginResponse
import com.example.zero.models.Usuario
import com.example.zero.pages.initial.InitialActivity
import com.example.zero.ui.theme.ZeroTheme
import com.example.zero.utils.usuarioActivo
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        enableEdgeToEdge()

        lifecycleScope.launch {
            val userStore = UserStore(this@LoginActivity)
            val datosGuardados = userStore.leerDatosUsuario.first()
            if (datosGuardados != "") {
                try {
                    val gson = Gson()
                    val loginResponse = gson.fromJson(datosGuardados, LoginResponse::class.java)
                    usuarioActivo = Usuario(
                        id_usuario = loginResponse.id_usuario ?: 0,
                        nombre_usuario = loginResponse.nombre ?: "",
                        rol = loginResponse.rol
                    )
                    startActivity(Intent(this@LoginActivity, InitialActivity::class.java))
                    finish()
                    return@launch
                } catch (_: Exception) {
                }
            }
            mostrarFormulario(viewModel)
        }
    }

    private fun mostrarFormulario(viewModel: LoginViewModel) {
        setContent {
            ZeroTheme {
                val uiState by viewModel.uiState.collectAsState()

                var mostrarDialog by remember { mutableStateOf(false) }
                var dialogExito by remember { mutableStateOf(false) }
                var dialogMensaje by remember { mutableStateOf("") }

                if (mostrarDialog) {
                    AlertDialog(
                        onDismissRequest = { mostrarDialog = false },
                        icon = {
                            Icon(
                                imageVector = if (dialogExito) Icons.Default.CheckCircle else Icons.Default.Clear,
                                contentDescription = null,
                                tint = if (dialogExito) Color(0xFF1A1A1A) else Color.Red,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        title = {
                            Text(
                                text = if (dialogExito) "Bienvenido" else "Error",
                                fontWeight = FontWeight.Bold,
                                color = if (dialogExito) Color(0xFF1A1A1A) else Color.Red
                            )
                        },
                        text = { Text(dialogMensaje) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    mostrarDialog = false
                                    if (dialogExito) {
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.putExtra("ya_autenticado", true)
                                        startActivity(intent)
                                        finish()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A))
                            ) {
                                Text("Aceptar", color = Color.White)
                            }
                        },
                        containerColor = Color.White,
                        iconContentColor = Color(0xFF1A1A1A)
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(24.dp)
                    ) {
                        Text("Iniciar sesión", style = MaterialTheme.typography.headlineLarge)

                        OutlinedTextField(
                            label = { Text("Email") },
                            value = viewModel.email,
                            onValueChange = { viewModel.email = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            label = { Text("Contraseña") },
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = viewModel.guardarSesion,
                                onCheckedChange = { viewModel.guardarSesion = it }
                            )
                            Text("Guardar inicio de sesión")
                        }

                        OutlinedButton(onClick = { viewModel.fetchLogin() }) {
                            Text("Iniciar sesión")
                        }

                        when (val state = uiState) {
                            is LoginUIState.Loading -> CircularProgressIndicator()
                            is LoginUIState.Error -> {
                                LaunchedEffect(state) {
                                    dialogExito = false
                                    dialogMensaje = state.message
                                    mostrarDialog = true
                                }
                            }
                            is LoginUIState.Success -> {
                                val resultado = state.resultado
                                LaunchedEffect(state) {
                                    if (resultado.success) {
                                        usuarioActivo = Usuario(
                                            id_usuario = resultado.id_usuario ?: 0,
                                            nombre_usuario = resultado.nombre ?: "",
                                            email_usuario = viewModel.email,
                                            rol = resultado.rol
                                        )
                                        if (viewModel.guardarSesion) {
                                            val gson = Gson()
                                            val userStore = UserStore(this@LoginActivity)
                                            userStore.guardarDatosUsuario(gson.toJson(resultado))
                                        }
                                        dialogExito = true
                                        dialogMensaje = "Bienvenido ${resultado.nombre}"
                                        mostrarDialog = true
                                    } else {
                                        dialogExito = false
                                        dialogMensaje = resultado.message ?: "Email o contraseña incorrectos"
                                        mostrarDialog = true
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}