package com.nixonsu.petrolpricenotifier.services

import com.nixonsu.petrolpricenotifier.clients.ElevenSevenClient
import com.nixonsu.petrolpricenotifier.clients.PetrolSpyClient
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PetrolPriceServiceTest {
    private val httpClient = mockk<HttpClient>()
    private val elevenSevenClient = ElevenSevenClient(httpClient)
    private val petrolSpyClient = PetrolSpyClient(httpClient)
    private val subject = PetrolPriceService(elevenSevenClient, petrolSpyClient)

    @Nested
    inner class GetLowestU91PriceForSevenElevenInAustralia {
        @Test
        fun `Given request is successful then return correct price`() {
            // Given
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody =
                PetrolPriceServiceTest::class.java.getResource("/fixtures/elevenSeven/response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            // When
            val price = subject.getLowestU91PriceForSevenElevenInAustralia()

            // Then
            assertEquals(177.5, price)
        }

        @Test
        fun `Given request is successful when response is malformed then return null`() {
            // Given
            val mockResponse: HttpResponse<String> = mockk()
            val mockResponseBody =
                PetrolPriceServiceTest::class.java.getResource("/fixtures/elevenSeven/malformed_response.json")?.readText(Charsets.UTF_8)
            every { mockResponse.body() } returns mockResponseBody
            every { mockResponse.statusCode() } returns 200
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            // When
            val price = subject.getLowestU91PriceForSevenElevenInAustralia()

            // Then
            assertNull(price)
        }

        @Test
        fun `Given request is not successful then return null`() {
            // Given
            val mockResponse: HttpResponse<String> = mockk()
            every { mockResponse.body() } returns ""
            every { mockResponse.statusCode() } returns 500
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            // When
            val price = subject.getLowestU91PriceForSevenElevenInAustralia()

            // Then
            assertNull(price)
        }
    }
}