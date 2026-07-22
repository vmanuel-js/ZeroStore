package com.example.zero

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.zero.data.local.UserStore
import com.example.zero.models.LoginResponse
import com.example.zero.models.Usuario
import com.example.zero.pages.chat.ChatViewModel
import com.example.zero.pages.initial.InitialActivity
import com.example.zero.pages.login.LoginActivity
import com.example.zero.ui.theme.ZeroTheme
import com.example.zero.utils.usuarioActivo
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )

        enableEdgeToEdge()
        setContent {
            ZeroTheme {
                var isVisible by remember { mutableStateOf(true) }
                val coroutineScope = rememberCoroutineScope()
                val context = LocalContext.current

                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painterResource(R.drawable.intro_image_man),
                        contentDescription = stringResource(R.string.inicio_imagen_descripcion),
                        modifier = Modifier
                            .fillMaxSize()
                            .drawWithContent {
                                drawContent()
                                drawRect(color = Color.Black.copy(alpha = 0.5f))
                            },
                        contentScale = ContentScale.Crop,
                    )
                    AnimatedVisibility(
                        visible = isVisible,
                        exit = slideOutHorizontally(
                            targetOffsetX = { fullWidth -> (fullWidth * 1.60).toInt() },
                            animationSpec = tween(500)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Image(
                                painterResource(R.drawable.zero_white),
                                contentDescription = stringResource(R.string.logo)
                            )
                            Text(
                                text = stringResource(R.string.inicio_titulo_principal),
                                color = MaterialTheme.colorScheme.surface,
                                style = MaterialTheme.typography.displayLarge,
                            )
                            Button(
                                onClick = {
                                    isVisible = false
                                    coroutineScope.launch {
                                        delay(600)
                                        val yaAutenticado = intent.getBooleanExtra("ya_autenticado", false)
                                        val userStore = UserStore(this@MainActivity)
                                        val datosGuardados = userStore.leerDatosUsuario.first()

                                        when {
                                            yaAutenticado -> {
                                                val i = Intent(this@MainActivity, InitialActivity::class.java)
                                                val options = ActivityOptionsCompat.makeCustomAnimation(
                                                    context, R.anim.slade_in_up, R.anim.stay
                                                )
                                                context.startActivity(i, options.toBundle())
                                            }
                                            datosGuardados != "" -> {
                                                val gson = com.google.gson.Gson()
                                                val loginResponse = gson.fromJson(datosGuardados, LoginResponse::class.java)
                                                usuarioActivo = Usuario(
                                                    id_usuario = loginResponse.id_usuario ?: 0,
                                                    nombre_usuario = loginResponse.nombre ?: "",
                                                    rol = loginResponse.rol
                                                )
                                                val intent = Intent(this@MainActivity, InitialActivity::class.java)
                                                val options = ActivityOptionsCompat.makeCustomAnimation(
                                                    context, R.anim.slade_in_up, R.anim.stay
                                                )
                                                context.startActivity(intent, options.toBundle())
                                            }
                                            else -> {
                                                // No hay sesión → va al login
                                                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                                                val options = ActivityOptionsCompat.makeCustomAnimation(
                                                    context, R.anim.slade_in_up, R.anim.stay
                                                )
                                                context.startActivity(intent, options.toBundle())
                                            }
                                        }
                                        (context as? Activity)?.finish()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(
                                    text = stringResource(R.string.inicio_boton),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}