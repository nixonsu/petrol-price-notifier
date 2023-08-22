package com.nixonsu

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.nixonsu.exceptions.PetrolPriceNotFoundException
import com.nixonsu.services.PetrolPriceService
import org.apache.http.client.HttpResponseException

class ApplicationHandler(
    private val petrolPriceService: PetrolPriceService,
    private val snsClient: AmazonSNS,
    private val snsTopicArn: String
) {
    fun handle(event: Map<String, Any>, context: Context) {
        val logger = context.logger

        logger.log("Received Event:\n${event}")

        logger.log("Calling petrol price service...")

        val lowestPrice = try {
            petrolPriceService.getLowestU91PriceInAustralia()
        } catch (e: HttpResponseException) {
            throw PetrolPriceNotFoundException("Error retrieving petrol price.", e)
        }
            ?: throw PetrolPriceNotFoundException(
                "Error retrieving petrol price - it is null. Petrol price api contract may have changed.",
                null
            )

        logger.log("Publishing to SNS...")
        val publishRequest = PublishRequest(snsTopicArn, "Lowest price for U91 today: $lowestPrice")
        val publishResponse = snsClient.publish(publishRequest)

        logger.log("Message published, message ID: ${publishResponse.messageId}")
    }
}