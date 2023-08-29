package com.nixonsu.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PetrolSpyUtilsTest {
    @Nested
    inner class ExtractU91PriceFromHtml {
        @Test
        fun `Given petrol spy html response then return U91 price`() {
            // Given
            val responseBody =
                PetrolSpyUtilsTest::class.java.getResource("/fixtures/petrolSpy/liberty_response.html")!!.readText(Charsets.UTF_8)

            // When
            val price = extractU91PriceFromHtml(responseBody)

            // Then
            assertEquals(199.7, price)
        }
    }
}