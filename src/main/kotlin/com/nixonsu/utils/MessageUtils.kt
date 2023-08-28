package com.nixonsu.utils

fun makeSmsMessage(stationToPrice: Map<String, Double?>): String {
    val stringBuilder = StringBuilder()

    val result = stationToPrice.entries.joinToString(separator = "\n") { "${it.key}: ${it.value ?: "N/A"}" }
    stringBuilder.append(result)

    return stringBuilder.toString()
}
