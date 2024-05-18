package com.nixonsu.petrolpricenotifier.services

import com.nixonsu.petrolpricenotifier.clients.ElevenSevenClient
import com.nixonsu.petrolpricenotifier.clients.PetrolSpyClient
import com.nixonsu.petrolpricenotifier.exceptions.UnableToRetrievePriceException
import com.nixonsu.petrolpricenotifier.models.Fuel
import com.nixonsu.petrolpricenotifier.models.Station
import com.nixonsu.petrolpricenotifier.models.Station.*
import com.nixonsu.petrolpricenotifier.utils.MalformedHtmlResponseException
import com.nixonsu.petrolpricenotifier.utils.extractLowestU91PriceAllRegions
import com.nixonsu.petrolpricenotifier.utils.extractU91PriceFromHtml
import org.apache.http.client.HttpResponseException
import org.slf4j.LoggerFactory

class PetrolPriceService(
    private val elevenSevenClient: ElevenSevenClient,
    private val petrolSpyClient: PetrolSpyClient
) {
    fun getLowestPriceFor(station: Station, fuel: Fuel): Double {
        return when (station) {
            SEVEN_ELEVEN -> getLowestPriceForSevenElevenFor(fuel)
            LIBERTY -> getLowestPriceForLibertyFor(fuel)
            COSTCO -> getLowestPriceForCostcoFor(fuel)
            BP -> getLowestPriceForBp(fuel)
        }
    }

    private fun getLowestPriceForSevenElevenFor(fuel: Fuel): Double {
        try {
            when (fuel) {
                Fuel.U91 -> {
                    val elevenSevenResponse = elevenSevenClient.getLowestSevenElevenPetrolPricesInAustralia()
                    return extractLowestU91PriceAllRegions(elevenSevenResponse)
                }
            }
        } catch (e: Exception) {
            throw UnableToRetrievePriceException("Error retrieving $fuel price of $SEVEN_ELEVEN", e)
        }
    }

    private fun getLowestPriceForLibertyFor(fuel: Fuel): Double {
        try {
            when (fuel) {
                Fuel.U91 -> {
                    val petrolSpyHtmlResponse = petrolSpyClient.getStationPricesHtmlFor(LIBERTY)
                    logger.info("done")
                    return extractU91PriceFromHtml(petrolSpyHtmlResponse)
                }
            }
        } catch (e: HttpResponseException) {
            throw UnableToRetrievePriceException("Error retrieving $fuel price of $LIBERTY", e)
        } catch (e: MalformedHtmlResponseException) {
            throw UnableToRetrievePriceException("Error parsing $fuel price of $LIBERTY", e)
        }
    }

    private fun getLowestPriceForCostcoFor(fuel: Fuel): Double {
        try {
            when (fuel) {
                Fuel.U91 -> {
                    val petrolSpyHtmlResponse = petrolSpyClient.getStationPricesHtmlFor(COSTCO)
                    return extractU91PriceFromHtml(petrolSpyHtmlResponse)
                }
            }
        } catch (e: Exception) {
            throw UnableToRetrievePriceException("Error retrieving $fuel price of $COSTCO", e)
        }
    }

    private fun getLowestPriceForBp(fuel: Fuel): Double {
        try {
            when (fuel) {
                Fuel.U91 -> {
                    val petrolSpyHtmlResponse = petrolSpyClient.getStationPricesHtmlFor(BP)
                    return extractU91PriceFromHtml(petrolSpyHtmlResponse)
                }
            }
        } catch (e: Exception) {
            throw UnableToRetrievePriceException("Error retrieving $fuel price of $BP", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PetrolPriceService::class.java)
    }
}
