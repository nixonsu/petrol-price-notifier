package com.nixonsu.clients

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.extensions.reasonPhrase
import com.nixonsu.models.ElevenSevenResponse
import org.apache.http.client.HttpResponseException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ElevenSevenClient(private val httpClient: HttpClient) {
    fun getLowestSevenElevenPetrolPricesInAustralia(): ElevenSevenResponse {
        val request = makeElevenSevenGetHttpRequest()

        println("Calling 11-Seven API: $request")
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        println("Received response: $response")

        if (response.statusCode() != 200) {
            throw HttpResponseException(response.statusCode(), response.reasonPhrase())
        }

        return objectMapper.readValue<ElevenSevenResponse>(response.body())
    }

    private fun makeElevenSevenGetHttpRequest(): HttpRequest? {
        return HttpRequest.newBuilder()
            .uri(URI.create(ELEVEN_SEVEN_URL))
            .GET()
            .build()
    }

    companion object {
        const val ELEVEN_SEVEN_URL = "https://projectzerothree.info/api.php?format=json"
        val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }
}