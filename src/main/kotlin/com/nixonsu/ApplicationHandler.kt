package com.nixonsu

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.nixonsu.exceptions.PetrolPriceCouldNotBeDeterminedException
import com.nixonsu.services.PetrolPriceService
import com.nixonsu.utils.makeSmsMessage

class ApplicationHandler(
    private val petrolPriceService: PetrolPriceService,
    private val snsClient: AmazonSNS,
    private val snsTopicArn: String
) {
    fun handle(event: Map<String, Any>, context: Context) {
        println("Received Event:\n${event}")

        println("Calling petrol price service...")

        val lowestPriceInAustralia = try {
            petrolPriceService.getLowestU91PriceInAustralia()
        } catch (e: PetrolPriceCouldNotBeDeterminedException) {
            println("Error retrieving lowest U91 price")
            null
        }

        val priceForSpecificStation = try {
            petrolPriceService.getU91PriceForSpecificStation()
        } catch (e: PetrolPriceCouldNotBeDeterminedException) {
            println("Error retrieving specific U91 price")
            null
        }

        val stationToPrice = mapOf(
            "7-Eleven" to lowestPriceInAustralia,
            "Liberty" to priceForSpecificStation
        )

        val message = makeSmsMessage(stationToPrice)

        val publishRequest = PublishRequest(snsTopicArn, message)
        println("Publishing to SNS with publish request:\n$publishRequest")
        val publishResponse = snsClient.publish(publishRequest)

        println("Message published, message ID: ${publishResponse.messageId}")
    }
}