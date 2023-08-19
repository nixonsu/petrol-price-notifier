package com.nixonsu.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.exceptions.HttpResponseException
import com.nixonsu.models.ElevenSevenResponse
import com.nixonsu.models.FuelType
import com.nixonsu.models.Region
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ElevenSevenService {
    companion object {
        const val ELEVEN_SEVEN_URL: String = "https://projectzerothree.info/api.php?format=json"
        val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }

    fun getRecentLowestU91PriceInAustralia(): Double? {
        val client = HttpClient.newBuilder().build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(ELEVEN_SEVEN_URL))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw HttpResponseException(response.statusCode(), "Unexpected response code ${response.statusCode()}")
        }

        val elevenSevenResponseBody: ElevenSevenResponse = objectMapper.readValue(response.body())

        return extractLowestU91PriceFromResponse(elevenSevenResponseBody)
    }

    private fun extractLowestU91PriceFromResponse(data: ElevenSevenResponse): Double? {
        val allRegions = data.regions.find { priceForRegion -> priceForRegion.region == Region.ALL }

        return allRegions?.prices?.find { entry -> entry.type == FuelType.U91 }?.price
    }
}