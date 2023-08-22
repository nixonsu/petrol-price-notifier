package com.nixonsu

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.nixonsu.exception.PetrolPriceNotFoundException
import com.nixonsu.service.PetrolPriceService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.http.client.HttpResponseException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ApplicationHandlerTest {
    private val petrolPriceService = mockk<PetrolPriceService>()
    private val snsClient = mockk<AmazonSNS>(relaxed = true)
    private val snsTopicArn = "xxxx"
    private val subject = ApplicationHandler(petrolPriceService, snsClient, snsTopicArn)
    private val context = mockk<Context>(relaxed = true)

    @Test
    fun `Given petrol price is retrieved successfully then publish to sns`() {
        // Given
        val expectedPublishRequest = PublishRequest(snsTopicArn, "Lowest price for U91 today: ${160.0}")
        every { petrolPriceService.getLowestU91PriceInAustralia() } returns 160.0

        // When
        subject.handle(emptyMap(), context)

        // Then
        verify(exactly = 1) { snsClient.publish(expectedPublishRequest) }
    }

    @Test
    fun `Given petrol price is not retrieved successfully then throw PetrolPriceNotFoundException`() {
        // Given
        every { petrolPriceService.getLowestU91PriceInAustralia() } throws HttpResponseException(500, "Internal Server Error")

        // When
        val exception = assertThrows<PetrolPriceNotFoundException> {
            subject.handle(emptyMap(), context)
        }

        // Then
        assertEquals("Error retrieving petrol price", exception.message)
        verify(exactly = 0) { snsClient.publish(any()) }
    }
}