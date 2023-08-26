package com.nixonsu.services

import com.nixonsu.clients.ElevenSevenClient
import com.nixonsu.models.ElevenSevenResponse

class PetrolPriceService(private val elevenSevenClient: ElevenSevenClient) {
    fun getLowestU91PriceInAustralia(): Double? {
        val elevenSevenResponse: ElevenSevenResponse = elevenSevenClient.getLowestSevenElevenPetrolPricesInAustralia()
        return extractLowestU91PriceAllRegions(elevenSevenResponse)
    }

    private fun extractLowestU91PriceAllRegions(data: ElevenSevenResponse): Double? {
        return data.regions.find { it.region == "All" }?.run {
            prices.find { it.type == "U91" }?.price
        }
    }
}