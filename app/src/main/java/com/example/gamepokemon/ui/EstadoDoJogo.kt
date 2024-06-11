package com.example.gamepokemon.ui

import androidx.compose.ui.graphics.Color

data class EstadoDoJogo(
    val palavraEmbaralhadaAtual: String = "",
    var errou: Boolean = false,
    val placar: Int = 0,
    val contadorPalavras: Int = 1,
    val gameOver: Boolean = false,
    val informacao: Boolean = false,
    val botaoInfo: Boolean = false,
    val acertou: Boolean = false,
    val selectedOptionBackgroundColor: Color = Color.Gray,
    val correctOptionBackgroundColor: Color = Color.Gray,
    val originalOptionBackgroundColor: Color = Color.Gray,
)