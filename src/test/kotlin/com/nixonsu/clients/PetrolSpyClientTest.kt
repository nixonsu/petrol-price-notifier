package com.nixonsu.clients

import com.nixonsu.services.PetrolPriceService
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.junit.jupiter.api.Test
import java.net.http.HttpClient

class PetrolSpyClientTest {
    @Test
    fun `fun`() {
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build()

        val petrolSpyClient = PetrolSpyClient(client)

        val elevenSevenClient = ElevenSevenClient(client)

        val petrolService = PetrolPriceService(elevenSevenClient, petrolSpyClient)
        
        println(petrolService.getU91PriceForSpecificStation())



    }
}