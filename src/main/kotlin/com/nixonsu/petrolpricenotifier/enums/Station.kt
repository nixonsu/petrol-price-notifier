package com.nixonsu.petrolpricenotifier.enums

enum class Station(private val stationName: String) {
    SEVEN_ELEVEN("7-Eleven"),
    LIBERTY("Liberty"),
    COSTCO("Costco"),
    BP("BP");

    override fun toString(): String {
        return stationName
    }
}