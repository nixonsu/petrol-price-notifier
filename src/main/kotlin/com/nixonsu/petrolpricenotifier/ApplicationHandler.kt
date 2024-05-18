package com.nixonsu.petrolpricenotifier

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.nixonsu.petrolpricenotifier.exceptions.UnableToRetrievePriceException
import com.nixonsu.petrolpricenotifier.models.Fuel
import com.nixonsu.petrolpricenotifier.models.Station
import com.nixonsu.petrolpricenotifier.services.PetrolPriceService
import com.nixonsu.petrolpricenotifier.utils.makeSmsMessage
import org.slf4j.LoggerFactory

class ApplicationHandler(
    private val petrolPriceService: PetrolPriceService,
    private val snsClient: AmazonSNS,
    private val snsTopicArn: String
) {
    fun handle(event: Map<String, Any>, context: Context) {
        logger.info("Received Event:\n${event}")
        logger.info("Fetching petrol prices...")

        val stations = enumValues<Station>()
        val stationPrices = stations.associateWith {
            try {
                petrolPriceService.getLowestPriceFor(it, Fuel.U91)
            } catch (e: UnableToRetrievePriceException) {
                logger.warn("Unable to retrieve a price", e)
                null
            }
        }
        logger.info("Finished fetching petrol prices")

        val message = makeSmsMessage(stationPrices)

        val publishRequest = PublishRequest(snsTopicArn, message)
        logger.info("Publishing to SNS with publish request: {}", publishRequest)
        val publishResponse = snsClient.publish(publishRequest)

        logger.info("Message published, message ID: {}", publishResponse.messageId)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ApplicationHandler::class.java)
    }
}
