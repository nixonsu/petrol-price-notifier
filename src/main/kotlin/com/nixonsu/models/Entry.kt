package com.nixonsu.models

data class Entry(
    val name: String,
    val type: FuelType,
    val price: Double,
    val suburb: String,
    val state: String,
    val postcode: String,
    val lat: Double,
    val lng: Double
)
