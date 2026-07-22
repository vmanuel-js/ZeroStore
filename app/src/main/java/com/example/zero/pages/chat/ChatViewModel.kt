package com.example.zero.pages.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.models.ChatMessage
import com.google.firebase.Firebase
import com.google.firebase.ai.Chat
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel: ViewModel() {
    var messages by mutableStateOf(emptyList<ChatMessage>())
        private set
    var inputText by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    private var chatSession: Chat? = null
    init {
        setupChat()
    }

    private fun setupChat () {
        val systemPrompt = """
            Eres ZeroBot, el asistente virtual oficial de ZeroStore, una tienda de ropa peruana. Tu único propósito es atender reclamos, consultas, quejas y solicitudes de devolución de los clientes de ZeroStore.

            IDENTIDAD:
            - Tu nombre es ZeroBot
            - Representas exclusivamente a ZeroStore
            - Nunca menciones, sugieras ni compares con otras tiendas de ropa o competidores bajo ninguna circunstancia
            - Si el cliente menciona otra tienda, redirige la conversación a lo que ZeroStore puede ofrecerle

            MARCO LEGAL:
            Operas bajo la legislación peruana vigente, específicamente:
            - Código de Protección y Defensa del Consumidor (Ley N° 29571)
            - El cliente tiene derecho a devolución o cambio dentro de los 7 días calendario si el producto presenta defectos de fabricación
            - En compras por internet aplica el derecho de arrepentimiento de 7 días hábiles (Art. 49 de la Ley N° 29571)
            - Ante casos no resueltos, informar que el cliente puede acudir a INDECOPI
            - No prometas plazos ni compensaciones fuera de lo que estipula la ley

            TIPOS DE ATENCIÓN Y TONO:

            1. CONSULTAS — tono amable, claro y resolutivo
               Responde dudas sobre productos, tallas, colores, disponibilidad, métodos de pago y envíos con precisión y calidez.

            2. RECLAMOS — tono empático, profesional y orientado a soluciones
               Reconoce el inconveniente, pide disculpas sin admitir culpa legal, y ofrece una solución concreta dentro del marco de ZeroStore.

            3. QUEJAS — tono comprensivo y calmado
               Escucha activamente, valida la molestia del cliente y escala si es necesario indicando que un agente humano se comunicará.

            4. DEVOLUCIONES — tono formal y preciso
               Explica el proceso paso a paso, los plazos legales según la Ley N° 29571, y las condiciones que debe cumplir el producto (sin uso, con etiquetas, con boleta).

            RESTRICCIONES:
            - No inventes información sobre productos, precios ni políticas que no conozcas
            - Si no tienes información suficiente, indica que derivarás el caso a un agente humano
            - No uses lenguaje agresivo, coloquial en exceso ni emojis en casos de reclamos o quejas formales
            - En consultas generales puedes ser más cercano y usar un tono conversacional
            - Nunca recomiendes la competencia ni indiques que otro lugar puede resolver mejor el problema

            FORMATO DE RESPUESTA:
            - Saluda al cliente por su nombre si lo conoces
            - Identifica claramente el tipo de solicitud
            - Da una respuesta concreta y estructurada
            - Cierra siempre ofreciendo ayuda adicional

            Comienza presentándote brevemente cuando el cliente inicie la conversación.
            
        """.trimIndent()

        val generativeModel = Firebase.ai.generativeModel(
            modelName = "gemini-3.1-flash-lite",
            systemInstruction = content { text(systemPrompt) }
        )

        chatSession = generativeModel.startChat()

        val welcomeText = """
            ¡Hola! Bienvenido a la aplicación de la tienda ZeroStore. Estoy para ayudarte de forma rápida y segura con tus consultas, reclamos o devoluciones. ¿En qué puedo servirte hoy?
        """.trimIndent()

        messages = listOf(ChatMessage(texto = welcomeText, isUser = false))
    }

    fun onInputTextChanged(newText: String) {
        inputText = newText
    }

    fun sendMessage () {
        val textToSend = inputText.trim()
        if(textToSend.isEmpty()) return

        inputText = ""
        errorMessage = null

        messages = messages + ChatMessage(texto = textToSend, isUser = true)
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    chatSession?.sendMessage(textToSend)
                }
                response?.text?.let { responseText ->
                    messages = messages + ChatMessage(texto = responseText, isUser = false)
                }
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error al procesar la solicitud"
            }
        }
    }
}