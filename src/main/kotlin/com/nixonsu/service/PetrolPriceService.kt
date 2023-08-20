package com.nixonsu.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.exception.HttpResponseException
import com.nixonsu.model.PetrolPriceResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PetrolPriceService {
    fun getLowestU91PriceInAustralia(): Double? {
        val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(ELEVEN_SEVEN_URL))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw HttpResponseException("Unexpected response code ${response.statusCode()}")
        }

        val petrolPriceResponse: PetrolPriceResponse = objectMapper.readValue(response.body())

        return extractLowestU91PriceAllRegions(petrolPriceResponse)
    }

    private fun extractLowestU91PriceAllRegions(data: PetrolPriceResponse): Double? {
        return data.regions.find { it.region == "All" }?.run {
            prices.find { it.type == "U91" }?.price
        }
    }

    companion object {
        const val ELEVEN_SEVEN_URL: String = "https://projectzerothree.info/api.php?format=json"
        val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }

}