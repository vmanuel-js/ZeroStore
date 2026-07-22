package com.example.zero.pages.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.zero.ui.theme.ZeroTheme

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContent {
            ZeroTheme {
                ChatScreen(
                    viewModel = viewModel,
                    onBack = { finish() }
                )
            }
        }
    }
}