package com.nixonsu

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent
import com.nixonsu.service.PetrolPriceService

class ApplicationHandler : RequestHandler<CloudWatchLogsEvent, String> {
    override fun handleRequest(event: CloudWatchLogsEvent, context: Context): String {
        val logger = context.logger

        logger.log("EVENT: \n${event.awsLogs.data}")

        logger.log("Calling petrol price service...")

        val petrolPriceService = PetrolPriceService()
        val lowestPrice = petrolPriceService.getLowestU91PriceInAustralia()

        println(lowestPrice)

        logger.log("Publishing to SNS...")

        logger.log("Done!")

        return "Finished"
    }
}
