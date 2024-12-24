package com.example.rxjavatasks.tasks2.other.network

data class CharacterResponse(
    val info: Info,
    val results: List<Character>
)

data class Info(
    val count: Int
)

data class Character(
    val id: Int,
    val name: String,
    val species: String
)

data class DiscountCard(
    val name: String,
    val discount: String
)