package com.nixonsu

import com.amazonaws.regions.Regions
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sns.model.PublishRequest
import com.nixonsu.service.PetrolPriceService

class ApplicationHandler : RequestHandler<Map<String, Any>, String> {
    override fun handleRequest(event: Map<String, Any>, context: Context): String {
        val logger = context.logger

        logger.log("Received Event:\n${event}")

        logger.log("Calling petrol price service...")

        val petrolPriceService = PetrolPriceService()
        val lowestPrice = petrolPriceService.getLowestU91PriceInAustralia()

        println(lowestPrice)

        logger.log("Publishing to SNS...")

        // Initialize the SNS Client
        val snsClient: AmazonSNS = AmazonSNSClientBuilder.standard()
            .withRegion(Regions.AP_SOUTHEAST_2)
            .build()

        // Define SNS Topic ARN
        val snsTopicArn = "xxx"

        // Create Publish Request
        val publishRequest = PublishRequest(snsTopicArn, "CloudWatch Event Triggered!", "CloudWatch Event")

        // Publish the message
        val publishResponse = snsClient.publish(publishRequest)
        logger.log("Message published, message ID: ${publishResponse.messageId}")

        return "Done"
    }
}
