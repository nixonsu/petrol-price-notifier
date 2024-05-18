package com.nixonsu.petrolpricenotifier.services

import com.nixonsu.petrolpricenotifier.clients.ElevenSevenClient
import com.nixonsu.petrolpricenotifier.clients.PetrolSpyClient
import com.nixonsu.petrolpricenotifier.exceptions.UnableToRetrievePriceException
import com.nixonsu.petrolpricenotifier.models.Fuel
import com.nixonsu.petrolpricenotifier.models.Station
import com.nixonsu.petrolpricenotifier.utils.MalformedHtmlResponseException
import io.mockk.every
import io.mockk.mockk
import org.apache.http.client.HttpResponseException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PetrolPriceServiceTest {
    private val httpClient = mockk<HttpClient>()
    private val elevenSevenClient = ElevenSevenClient(httpClient)
    private val petrolSpyClient = PetrolSpyClient(httpClient)
    private val subject = PetrolPriceService(elevenSevenClient, petrolSpyClient)

    @Nested
    inner class GetLowestPriceForSevenElevenU91 {
        @Test
        fun `When request successful should return correct price`() {
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
            val price = subject.getLowestPriceFor(Station.SEVEN_ELEVEN, Fuel.U91)

            // Then
            assertEquals(177.5, price)
        }

        @Test
        fun `When request unsuccessful should throw exception`() {
            // Given
            val mockResponse: HttpResponse<String> = mockk()
            every { mockResponse.body() } returns ""
            every { mockResponse.statusCode() } returns 500
            every {
                httpClient.send<String>(any<HttpRequest>(), any())
            } returns mockResponse

            // When
            // Then
            val exception = assertThrows<UnableToRetrievePriceException> {
                subject.getLowestPriceFor(Station.SEVEN_ELEVEN, Fuel.U91)
            }
            val cause = exception.cause as HttpResponseException
            assertEquals(500, cause.statusCode)
            assertEquals("Internal Server Error", cause.reasonPhrase)
        }
    }

    @Nested
    inner class GetLowestPriceForLibertyU91 {
        @Test
        fun `When request successful, should return correct price`() {
            // Given
            val mockResponse: HttpResponse<ByteArray> = mockk()
            val mockResponseBody =
                PetrolPriceServiceTest::class.java.getResource("/fixtures/petrolSpy/liberty_response.html")!!.readText(Charsets.UTF_8)

            every { mockResponse.body() } returns mockResponseBody.toByteArray()
            every { mockResponse.statusCode() } returns 200
            every { mockResponse.headers() } returns HttpHeaders.of(mapOf()) { _, _ -> true }
            every {
                httpClient.send(any<HttpRequest>(), any<HttpResponse.BodyHandler<ByteArray>>())
            } returns mockResponse

            // When
            val price = subject.getLowestPriceFor(Station.LIBERTY, Fuel.U91)

            // Then
            assertEquals(199.7, price)
        }
    }
}
