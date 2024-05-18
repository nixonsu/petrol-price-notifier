package com.nixonsu.petrolpricenotifier.utils

import com.nixonsu.petrolpricenotifier.models.Station

fun makeSmsMessage(stationToPrice: Map<Station, Double?>): String {
    val stringBuilder = StringBuilder()

    val hasNonNullPrice = stationToPrice.any { it.value != null }

    val lowestPriceStation = if (hasNonNullPrice) {
        stationToPrice.minByOrNull { it.value ?: Double.POSITIVE_INFINITY }?.key
    } else {
        null
    }

    val result = stationToPrice.entries.joinToString(separator = "\n") { entry ->
        val star = if (entry.key == lowestPriceStation) " ⭐" else ""
        "${entry.key}: ${entry.value ?: "N/A"}$star"
    }

    stringBuilder.append(result)

    return stringBuilder.toString()
}
