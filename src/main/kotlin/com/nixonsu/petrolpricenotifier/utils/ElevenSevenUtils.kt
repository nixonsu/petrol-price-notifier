package com.nixonsu.petrolpricenotifier.utils

import com.nixonsu.petrolpricenotifier.models.ElevenSevenResponse

fun extractLowestU91PriceAllRegions(data: ElevenSevenResponse): Double? {
    return data.regions.find { it.region == "All" }?.run {
        prices.find { it.type == "U91" }?.price
    }
}
