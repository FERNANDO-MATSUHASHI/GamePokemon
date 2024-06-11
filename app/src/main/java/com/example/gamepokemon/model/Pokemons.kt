package com.example.apppokemon.model

import kotlinx.serialization.Serializable

@Serializable
data class Pokemons(
    val results: List<Pokemon>
)

@Serializable
data class Pokemon(
    val name: String,
    val url: String
)

//// Pegar URL da Imagem do Pokemon
@Serializable
data class Sprites(
    val sprites: PokemonOther,
    val weight: Int,
    val height: Int
)

@Serializable
data class PokemonOther(
    val other: PokemonHome
)

@Serializable
data class PokemonHome(
    val home: PokemonImg
)

@Serializable
data class PokemonImg(
    val front_default: String,
    val front_shiny: String
)