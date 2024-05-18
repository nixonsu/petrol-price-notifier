package com.nixonsu.petrolpricenotifier

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.nixonsu.petrolpricenotifier.exceptions.UnableToRetrievePriceException
import com.nixonsu.petrolpricenotifier.models.Fuel
import com.nixonsu.petrolpricenotifier.models.Station.*
import com.nixonsu.petrolpricenotifier.services.PetrolPriceService
import com.nixonsu.petrolpricenotifier.utils.makeSmsMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ApplicationHandlerTest {
    private val petrolPriceService = mockk<PetrolPriceService>()
    private val snsClient = mockk<AmazonSNS>(relaxed = true)
    private val snsTopicArn = "xxxx"
    private val subject = ApplicationHandler(petrolPriceService, snsClient, snsTopicArn)
    private val context = mockk<Context>(relaxed = true)

    @Test
    fun `Given petrol prices are retrieved successfully then publish message to sns`() {
        // Given
        val sevenElevenPrice = 160.0
        val libertyPrice = 170.0
        val costcoPrice = 165.0
        val bpPrice = 200.0
        val expectedMessage = makeSmsMessage(
            mapOf(
                SEVEN_ELEVEN to sevenElevenPrice,
                LIBERTY to libertyPrice,
                COSTCO to costcoPrice,
                BP to bpPrice
            )
        )
        val expectedPublishRequest = PublishRequest(snsTopicArn, expectedMessage)
        every { petrolPriceService.getLowestPriceFor(SEVEN_ELEVEN, Fuel.U91) } returns sevenElevenPrice
        every { petrolPriceService.getLowestPriceFor(LIBERTY, Fuel.U91) } returns libertyPrice
        every { petrolPriceService.getLowestPriceFor(COSTCO, Fuel.U91) } returns costcoPrice
        every { petrolPriceService.getLowestPriceFor(BP, Fuel.U91) } returns bpPrice

        // When
        subject.handle(emptyMap(), context)

        // Then
        verify(exactly = 1) { snsClient.publish(expectedPublishRequest) }
    }

    @Test
    fun `Given petrol prices are not retrieved successfully then still publish message to sns`() {
        // Given
        every { petrolPriceService.getLowestPriceFor(SEVEN_ELEVEN, Fuel.U91) } throws UnableToRetrievePriceException("", null)
        every { petrolPriceService.getLowestPriceFor(LIBERTY, Fuel.U91) } throws UnableToRetrievePriceException("", null)
        every { petrolPriceService.getLowestPriceFor(COSTCO, Fuel.U91) } throws UnableToRetrievePriceException("", null)
        every { petrolPriceService.getLowestPriceFor(BP, Fuel.U91) } throws UnableToRetrievePriceException("", null)
        val expectedMessage = makeSmsMessage(
            mapOf(
                SEVEN_ELEVEN to null,
                LIBERTY to null,
                COSTCO to null,
                BP to null
            )
        )
        val expectedPublishRequest = PublishRequest(snsTopicArn, expectedMessage)

        // When
        subject.handle(emptyMap(), context)

        // Then
        verify(exactly = 1) { snsClient.publish(expectedPublishRequest) }
    }
}
