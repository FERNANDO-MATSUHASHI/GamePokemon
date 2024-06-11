package com.example.apppokemon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppokemon.model.Pokemon
import com.example.gamepokemon.api.InstanciaRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

var imagemUrl by mutableStateOf("")
var infoPeso by mutableStateOf("")
var infoAltura by mutableStateOf("")

class PokemonsViewModel : ViewModel() {
    private val _pokemons = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemons: StateFlow<List<Pokemon>> = _pokemons

    private val _pokemonsPeso = MutableStateFlow<String?>(null)
    private val _pokemonsAltura = MutableStateFlow<String?>(null)
    private val _pokemonImagem = MutableStateFlow<String?>(null)
    val pokemonsPeso: MutableStateFlow<String?> = _pokemonsPeso
    val pokemonsAltura: MutableStateFlow<String?> = _pokemonsAltura
    val pokemonImagem: MutableStateFlow<String?> = _pokemonImagem

    fun obterPokemons() {
        viewModelScope.launch {
            try {
                val response = InstanciaRetrofit.api.getPokemons(limit = 20, offset = 0)
                _pokemons.value = response.results
                println("API Call - ${response.results}")
            } catch (e: Exception) {
                println("API Call - Failed to fetch data ${e}")
            }
        }
    }

    fun obterPokemonPesoAltura(name: String) {
        viewModelScope.launch {
            try {
                val responsePesoAltura = InstanciaRetrofit.apiPesoAltura.getPokemonsPesoAltura(name)
                pokemonsPeso.value = responsePesoAltura.height.toString()
                pokemonsAltura.value = responsePesoAltura.weight.toString()
                infoPeso = pokemonsPeso.value.toString()
                infoAltura = pokemonsAltura.value.toString()

                println("API Call Peso - ${infoPeso}")
                println("API Call ALtura - ${infoAltura}")
            } catch (e: Exception) {
                println("API Peso/Altura Call - Failed to fetch data ${e}")
            }
        }
    }

    fun obterPokemonImagem(name: String) {
        viewModelScope.launch {
            try {
                val responseImagem = InstanciaRetrofit.apiImagem.getPokemonImagem(name)
                pokemonImagem.value = responseImagem.sprites.other.home.front_default
                imagemUrl = pokemonImagem.value.toString()

                //println("Name: ${name}")
                //println("API Call Imagem - ${pokemonImagem.value}")
            } catch (e: Exception) {
                println("API Call - Failed to fetch data ${e}")
            }
        }
    }
}
