package com.nixonsu.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.extensions.reasonPhrase
import com.nixonsu.models.PetrolPriceResponse
import org.apache.http.client.HttpResponseException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PetrolPriceService(private val httpClient: HttpClient) {
    fun getLowestU91PriceInAustralia(): Double? {
        val request = makeHttpPetrolServiceHttpRequest()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw HttpResponseException(response.statusCode(), response.reasonPhrase())
        }

        val petrolPriceResponse: PetrolPriceResponse = objectMapper.readValue(response.body())

        return extractLowestU91PriceAllRegions(petrolPriceResponse)
    }

    private fun makeHttpPetrolServiceHttpRequest(): HttpRequest? {
        return HttpRequest.newBuilder()
            .uri(URI.create(ELEVEN_SEVEN_URL))
            .GET()
            .build()
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