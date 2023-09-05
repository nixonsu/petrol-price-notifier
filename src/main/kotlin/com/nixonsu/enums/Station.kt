package com.nixonsu.enums

enum class Station(private val stationName: String) {
    SEVEN_ELEVEN("7-Eleven"),
    LIBERTY("Liberty"),
    COSTCO("Costco");

    override fun toString(): String {
        return stationName
    }
}