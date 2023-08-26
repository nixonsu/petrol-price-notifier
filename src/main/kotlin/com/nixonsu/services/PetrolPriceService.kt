package com.nixonsu.services

import com.nixonsu.clients.ElevenSevenClient
import com.nixonsu.clients.PetrolSpyClient
import com.nixonsu.models.ElevenSevenResponse
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.regex.Pattern

class PetrolPriceService(
    private val elevenSevenClient: ElevenSevenClient,
    private val petrolSpyClient: PetrolSpyClient
) {
    fun getLowestU91PriceInAustralia(): Double? {
        val elevenSevenResponse = elevenSevenClient.getLowestSevenElevenPetrolPricesInAustralia()
        return extractLowestU91PriceAllRegions(elevenSevenResponse)
    }

    fun getU91PriceForSpecificStation(): Double? {
        val petrolSpyResponse = petrolSpyClient.getSpecificStationPricesHtml()

        val priceAsString = extractU91PriceFromHtml(petrolSpyResponse)

        return priceAsString?.toDouble()
    }

    private fun extractU91PriceFromHtml(petrolSpyResponse: String): String? {
        val document = Jsoup.parse(petrolSpyResponse)
        val pricesList: Element? = document.getElementById("prices-list")

        if (pricesList != null) {
            for (price in pricesList.children()) {
                val pattern = Pattern.compile("Unleaded 91\\s+(\\d+\\.\\d+)")
                val matcher = pattern.matcher(price.text())

                if (matcher.find()) {
                    return matcher.group(1)
                }
            }
        }

        return null
    }

    private fun extractLowestU91PriceAllRegions(data: ElevenSevenResponse): Double? {
        return data.regions.find { it.region == "All" }?.run {
            prices.find { it.type == "U91" }?.price
        }
    }
}