package com.nixonsu.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.nixonsu.models.ElevenSevenResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ElevenSevenUtilsTest {

    @Nested
    inner class ExtractLowestU91PriceAllRegions {
        private val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())

        @Test
        fun `Given 11-Seven response body with lowest petrol prices then return lowest U91 price out of all regions`() {
            val responseBody =
                ElevenSevenUtilsTest::class.java.getResource("/fixtures/elevenSeven/response.json")!!.readText(Charsets.UTF_8)
            val elevenSevenResponse = objectMapper.readValue<ElevenSevenResponse>(responseBody)

            // When
           val price = extractLowestU91PriceAllRegions(elevenSevenResponse)

            // Then
            assertEquals(177.5, price)
        }
    }

}