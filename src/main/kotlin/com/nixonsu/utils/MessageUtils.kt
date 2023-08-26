package com.nixonsu.utils

fun makeSmsMessage(stationToPrice: Map<String, Double?>): String {
    val stringBuilder = StringBuilder()

    stringBuilder.append("Prices for the day")
    stringBuilder.append("\n")

    stationToPrice.forEach{
        stringBuilder.append("${it.key}: ${it.value}")
        stringBuilder.append("\n")
    }

    return stringBuilder.toString()
}