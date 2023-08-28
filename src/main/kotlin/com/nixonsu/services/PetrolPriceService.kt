package com.nixonsu.services

import com.nixonsu.clients.ElevenSevenClient
import com.nixonsu.clients.PetrolSpyClient
import com.nixonsu.exceptions.PetrolPriceCouldNotBeDeterminedException
import com.nixonsu.utils.extractLowestU91PriceAllRegions
import com.nixonsu.utils.extractU91PriceFromHtml

class PetrolPriceService(
    private val elevenSevenClient: ElevenSevenClient,
    private val petrolSpyClient: PetrolSpyClient
) {
    fun getLowestU91PriceInAustralia(): Double? {
        try {
            val elevenSevenResponse = elevenSevenClient.getLowestSevenElevenPetrolPricesInAustralia()
            return extractLowestU91PriceAllRegions(elevenSevenResponse)
        } catch (e: Exception) {
            throw PetrolPriceCouldNotBeDeterminedException("Error retrieving petrol prices", e)
        }
    }

    fun getU91PriceForSpecificStation(): Double? {
        try {
            val petrolSpyResponse = petrolSpyClient.getSpecificStationPricesHtml()

            val priceAsString = extractU91PriceFromHtml(petrolSpyResponse)

            return priceAsString?.toDouble()
        } catch (e: Exception) {
            throw PetrolPriceCouldNotBeDeterminedException("Error retrieving petrol prices", e)
        }
    }
}
