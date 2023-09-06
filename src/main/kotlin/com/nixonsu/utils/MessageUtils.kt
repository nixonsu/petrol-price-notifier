package com.nixonsu.utils

import com.nixonsu.enums.Station

fun makeSmsMessage(stationToPrice: Map<Station, Double?>): String {
    val stringBuilder = StringBuilder()

    val hasNonNullPrice = stationToPrice.any { it.value != null }

    val lowestPriceStation = if (hasNonNullPrice) {
        stationToPrice.minByOrNull { it.value ?: Double.POSITIVE_INFINITY }?.key
    } else {
        null
    }

    val result = stationToPrice.entries.joinToString(separator = "\n") { entry ->
        val star = if (entry.key == lowestPriceStation) "⭐" else ""
        String.format("%-10s %-5s %3s", entry.key, entry.value ?: "N/A", star)
    }

    stringBuilder.append(result)

    return stringBuilder.toString()
}
