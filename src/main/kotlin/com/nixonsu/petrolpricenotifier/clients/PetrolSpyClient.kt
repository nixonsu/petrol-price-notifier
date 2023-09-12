package com.nixonsu.petrolpricenotifier.clients

import com.nixonsu.petrolpricenotifier.enums.Station
import com.nixonsu.petrolpricenotifier.enums.Station.*
import com.nixonsu.petrolpricenotifier.extensions.reasonPhrase
import org.apache.http.client.HttpResponseException
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

class PetrolSpyClient(private val httpClient: HttpClient) {
    fun getStationPricesHtmlFor(station: Station): String {

        val request = stationToUrl[station]?.let { makePetrolSpyGetRequest(it) }

        logger.info("Calling PetrolSpy: {}", request)

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray())

        logger.info("Received response: {}", response)

        if (response.statusCode() == 200) {
            return decodeContent(response)

        } else {
            throw HttpResponseException(response.statusCode(), response.reasonPhrase())
        }
    }

    private fun decodeContent(response: HttpResponse<ByteArray>): String {
        val contentEncoding = response.headers().firstValue("Content-Encoding").orElse("")

        return if (contentEncoding == "gzip") {
            // Handle gzip compressed response
            val byteArrayInputStream = ByteArrayInputStream(response.body())
            val gzipStream = GZIPInputStream(byteArrayInputStream)
            val reader = InputStreamReader(gzipStream, StandardCharsets.UTF_8)
            reader.readText()
        } else {
            // Handle uncompressed response
            String(response.body(), StandardCharsets.UTF_8)
        }
    }

    private fun makePetrolSpyGetRequest(url: String): HttpRequest? {
        return HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .build()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PetrolSpyClient::class.java)

        private val stationToUrl = mapOf(
            LIBERTY to "https://petrolspy.com.au/map/station/58ae945fe4b0435d6f15971b",
            COSTCO to "https://petrolspy.com.au/map/station/569c791974770a18583e6964",
            BP to "https://petrolspy.com.au/map/station/5212adc80364fe88b97858e9"
        )
    }
}