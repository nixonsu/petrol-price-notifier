package com.nixonsu

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.nixonsu.enums.Station
import com.nixonsu.enums.Station.*
import com.nixonsu.services.PetrolPriceService
import com.nixonsu.utils.makeSmsMessage
import org.slf4j.LoggerFactory

class ApplicationHandler(
    private val petrolPriceService: PetrolPriceService,
    private val snsClient: AmazonSNS,
    private val snsTopicArn: String
) {
    fun handle(event: Map<String, Any>, context: Context) {
        logger.info("Received Event:\n${event}")

        logger.info("Fetching petrol prices...")

        val sevenElevenPrice = petrolPriceService.getLowestU91PriceForSevenElevenInAustralia()

        val libertyPrice = petrolPriceService.getU91PriceForLiberty()

        val costcoPrice = petrolPriceService.getU91PriceForCostco()

        val stationToPrice = mapOf(
            SEVEN_ELEVEN to sevenElevenPrice,
            LIBERTY to libertyPrice,
            COSTCO to costcoPrice
        )

        logger.info("Finished fetching petrol prices")

        val message = makeSmsMessage(stationToPrice)

        val publishRequest = PublishRequest(snsTopicArn, message)
        logger.info("Publishing to SNS with publish request: {}", publishRequest)
        val publishResponse = snsClient.publish(publishRequest)

        logger.info("Message published, message ID: {}", publishResponse.messageId)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ApplicationHandler::class.java)
    }
}