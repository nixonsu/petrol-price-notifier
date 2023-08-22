package com.nixonsu

import com.amazonaws.regions.Regions
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.nixonsu.services.PetrolPriceService
import java.net.http.HttpClient

class RuntimeHandler : RequestHandler<Map<String, Any>, String> {
    override fun handleRequest(event: Map<String, Any>, context: Context): String {
        // Initialise dependencies
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build()
        val petrolPriceService = PetrolPriceService(client)
        val snsClient: AmazonSNS = AmazonSNSClientBuilder.standard()
            .withRegion(Regions.AP_SOUTHEAST_2)
            .build()
        val snsTopicArn = System.getenv("SNS_TOPIC_ARN")

        // Inject dependencies
        val applicationHandler = ApplicationHandler(petrolPriceService, snsClient, snsTopicArn)

        // Handle event
        applicationHandler.handle(event, context)

        return "Done"
    }
}
