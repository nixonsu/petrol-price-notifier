package com.nixonsu.model

data class PetrolPriceResponse(
    val updated: Long,
    val regions: List<Region>
)

data class Region(
    val region: String,
    val prices: List<Price>
)

data class Price(
    val name: String,
    val type: String,
    val price: Double,
    val suburb: String,
    val state: String,
    val postcode: String,
    val lat: Double,
    val lng: Double
)
