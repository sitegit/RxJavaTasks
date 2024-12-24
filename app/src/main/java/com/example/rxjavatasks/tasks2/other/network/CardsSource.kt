package com.example.rxjavatasks.tasks2.other.network

import io.reactivex.rxjava3.core.Single

class CardsSource {

    val server1Cards = Single.just(
        listOf(
            DiscountCard("Card1", "10%"),
            DiscountCard("Card2", "15%"),
            DiscountCard("Card3", "20%"),
            DiscountCard("Card4", "25%"),
            DiscountCard("Card5", "30%"),
        )
    )

    val server2Cards = Single.just(
        listOf(
            DiscountCard("Card6", "5%"),
            DiscountCard("Card7", "3%"),
            DiscountCard("Card8", "7%"),
            DiscountCard("Card9", "2%"),
            DiscountCard("Card10", "9%")
        )
    )//.doOnSubscribe { throw RuntimeException("Не удалось получить данные от сервера 2") }
}