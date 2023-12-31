package com.nixonsu.petrolpricenotifier.services

import com.nixonsu.petrolpricenotifier.clients.ElevenSevenClient
import com.nixonsu.petrolpricenotifier.clients.PetrolSpyClient
import com.nixonsu.petrolpricenotifier.enums.Station.*
import com.nixonsu.petrolpricenotifier.utils.extractLowestU91PriceAllRegions
import com.nixonsu.petrolpricenotifier.utils.extractU91PriceFromHtml
import org.slf4j.LoggerFactory

class PetrolPriceService(
    private val elevenSevenClient: ElevenSevenClient,
    private val petrolSpyClient: PetrolSpyClient
) {
    fun getLowestU91PriceForSevenElevenInAustralia(): Double? {
        try {
            val elevenSevenResponse = elevenSevenClient.getLowestSevenElevenPetrolPricesInAustralia()
            return extractLowestU91PriceAllRegions(elevenSevenResponse)
        } catch (e: Exception) {
            logger.warn("Error retrieving U91 price of 7-Eleven", e)
        }

        return null
    }

    fun getU91PriceForLiberty(): Double? {
        try {
            val petrolSpyHtmlResponse = petrolSpyClient.getStationPricesHtmlFor(LIBERTY)

            return extractU91PriceFromHtml(petrolSpyHtmlResponse)
        } catch (e: Exception) {
            logger.warn("Error retrieving U91 price of Liberty", e)
        }

        return null
    }

    fun getU91PriceForCostco(): Double? {
        try {
            val petrolSpyHtmlResponse = petrolSpyClient.getStationPricesHtmlFor(COSTCO)

            return extractU91PriceFromHtml(petrolSpyHtmlResponse)
        } catch (e: Exception) {
            logger.warn("Error retrieving U91 price of Costco", e)
        }

        return null
    }

    fun getU91PriceForBp(): Double? {
        try {
            val petrolSpyHtmlResponse = petrolSpyClient.getStationPricesHtmlFor(BP)

            return extractU91PriceFromHtml(petrolSpyHtmlResponse)
        } catch (e: Exception) {
            logger.warn("Error retrieving U91 price of Costco", e)
        }

        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PetrolPriceService::class.java)
    }
}
