package com.example.gamepokemon.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gamepokemon.dados.NUMERO_DE_PALAVRAS
import com.example.gamepokemon.dados.PONTO_ACERTO
import com.example.gamepokemon.dados.pokemonsDisponiveis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.timer

var pokemonAtual by mutableStateOf("")
var listOption = mutableListOf<String>()

class JogoViewModel: ViewModel() {
    // Criamos um objeto PRIVADO contendo o estado atual do jogo, usando
    // como base a definição existente na classe EstadoDoJogo.
    // Colocar underline no começo de uma variável privada é uma "boa prática"
    // Perceba que é uma versão MUTÁVEL do fluxo de estado
    private val _estadoAtual = MutableStateFlow(EstadoDoJogo())

    // Criamos uma variável ABERTA (PÚBLICA) contendo uma cópia somente leitura
    // do estado do jogo atual
    val estadoAtual: StateFlow<EstadoDoJogo> = _estadoAtual.asStateFlow()

    // Variável que armazena a palavra sendo adivinhada no momento (não embaralhada)
    private lateinit var palavraAtual: String

    // Variável que armazena o palpite do jogador (o que ele digitou no OutlinedTextField)
    var palpiteJogador by mutableStateOf("")

    // Função para atualizar a avariável de estado "palpiteJogador"

    fun atualizarPalpiteJogador(palavra: String) {
        palpiteJogador = palavra
    }

    fun atualizarEstado(placarAtualizado: Int) {
        if (estadoAtual.value.contadorPalavras == NUMERO_DE_PALAVRAS) {
            // Estamos na última rodada do jogo
            _estadoAtual.update { currentState ->
                currentState.copy(
                    errou = false,
                    placar = placarAtualizado,
                    gameOver = true
                )
            }
        } else {
            // Rodada normal do jogo
            _estadoAtual.update { currentState ->
                currentState.copy(
                    errou = false,
                    //palavraEmbaralhadaAtual = sortearEmbaralharPalavra(),
                    placar = placarAtualizado,
                    contadorPalavras = currentState.contadorPalavras.inc()
                )
            }
        }
    }

    // Verifica se o jogador acertou ou errou
    fun verificarAcerto(option: String) {
        if (option == palavraAtual) {
            // Aumentamos os pontos do placar
            val placarAtualizado = _estadoAtual.value.placar.plus(PONTO_ACERTO)

            _estadoAtual.update { currentState ->
                currentState.copy(
                    informacao = true,
                    botaoInfo = true,
                    acertou = true
                )
            }

            atualizarEstado((placarAtualizado))
        } else {
            // jogador errou. Modamos a variável de estado "errou"
            _estadoAtual.update { currentState ->
                currentState.copy(
                    errou = true,
                    informacao = true,
                    botaoInfo = true
                )
            }
        }
        // redefinir o palpite dele (para limpar o TextField)
        atualizarPalpiteJogador("")
    }

    // Vai para a próxima jogada
    fun proximo() {
        if (estadoAtual.value.contadorPalavras == NUMERO_DE_PALAVRAS) {
            // Estamos na última rodada do jogo
            _estadoAtual.update { currentState ->
                currentState.copy(
                    errou = false,
                    informacao = false,
                    gameOver = true
                )
            }
        } else {
            // Rodada normal do jogo
            _estadoAtual.update { currentState ->
                currentState.copy(
                    errou = false,
                    palavraEmbaralhadaAtual = sortearEmbaralharPalavra(),
                    informacao = false,
                    acertou = false,
                    contadorPalavras = currentState.contadorPalavras.inc()
                )
            }
        }
    }

    // Pula a jogada
    fun pularJogada() {
        if (estadoAtual.value.contadorPalavras == NUMERO_DE_PALAVRAS) {
            // Estmos na última rodada do jogo
            _estadoAtual.update { currentState ->
                currentState.copy(
                    errou = false,
                    // placar = placarAtualizado,
                    gameOver = true
                )
            }
        } else {
            // Rodada normal do jogo
            _estadoAtual.update { currentState ->
                currentState.copy(
                    errou = false,
                    palavraEmbaralhadaAtual = sortearEmbaralharPalavra(),
                    // placar = placarAtualizado,
                    contadorPalavras = currentState.contadorPalavras.inc()
                )
            }
        }
    }
    init {
        reiniciarJogo()
    }
    fun reiniciarJogo() {
        // Definimos uma nova palavra embaralhada atual para ser adivinhada
        _estadoAtual.value = EstadoDoJogo(palavraEmbaralhadaAtual = sortearEmbaralharPalavra())
    }

    fun sortearEmbaralharPalavra(): String {
        // Obtemos em "palavrasDisponiveis" (arquivo dados/Palavras.kt) uma
        // palavra aleatório

        val options = mutableListOf<String>().apply {
            val wordsSet = mutableSetOf<String>()
            for (i in 1..4) {
                palavraAtual = pokemonsDisponiveis.random()

                if (palavraAtual !in wordsSet) {
                    add(palavraAtual)
                    wordsSet.add(palavraAtual)
                }
            }
        }
        //println("Lista: ${options}")
        listOption = options
        palavraAtual = listOption.random()

        pokemonAtual = palavraAtual

        // No Kotlin, não há função pronta para embaralhar uma String. Por isso
        // criamos uma variável temporária contendo um "vetor" dos caracteres da
        // string "palavraAtual"
        val vetorDeCharTemporario = palavraAtual.toCharArray()
        // Embaralha o vetor de char
        vetorDeCharTemporario.shuffle()
        // Retornamos agora uma nova String criada a partir do vetor de char
        return  String(vetorDeCharTemporario)
    }
}