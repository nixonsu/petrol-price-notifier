package com.nixonsu

import com.nixonsu.service.PetrolPriceService

fun main(args: Array<String>) {
    val petrolPriceService = PetrolPriceService()

    println(petrolPriceService.getLowestU91PriceInAustralia())
}
