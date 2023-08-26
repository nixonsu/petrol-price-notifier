package com.nixonsu.clients

import com.nixonsu.extensions.reasonPhrase
import org.apache.http.client.HttpResponseException
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

class PetrolSpyClient(private val httpClient: HttpClient) {
    fun getSpecificStationPricesHtml(): String {
        val request = makePetrolSpyGetRequest()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray())

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

    private fun makePetrolSpyGetRequest(): HttpRequest? {
        return HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(PETROL_SPY_SPECIFIC_STATION_URL))
            .build()
    }


    companion object {
        private const val PETROL_SPY_SPECIFIC_STATION_URL =
            "https://petrolspy.com.au/map/station/58ae945fe4b0435d6f15971b"
    }
}