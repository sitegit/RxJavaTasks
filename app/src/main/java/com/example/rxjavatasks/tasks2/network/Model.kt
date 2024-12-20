package com.example.rxjavatasks.tasks2.network

data class CharacterResponse(
    val info: Info,
    val results: List<Character>
)

data class Info(
    val count: Int
)

data class Character(
 val name: String,
 val species: String
)