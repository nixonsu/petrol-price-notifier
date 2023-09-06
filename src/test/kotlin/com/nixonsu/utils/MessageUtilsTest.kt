package com.nixonsu.utils

import com.nixonsu.enums.Station
import com.nixonsu.enums.Station.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MessageUtilsTest {
    @Nested
    inner class MakeSmsMessage {
        @Test
        fun `Given map of station to price then return sms message with star next to lowest price`() {
            val sevenElevenPrice = 170.0
            val libertyPrice = 190.5
            val costcoPrice = 182.5
            val stationToPrice: Map<Station, Double?> = mapOf(
                SEVEN_ELEVEN to sevenElevenPrice,
                LIBERTY to libertyPrice,
                COSTCO to costcoPrice
            )

            val message = makeSmsMessage(stationToPrice)

            val expectedMessage =
                "$SEVEN_ELEVEN   $sevenElevenPrice   ⭐\n$LIBERTY    $libertyPrice    \n$COSTCO     $costcoPrice    "
            assertEquals(expectedMessage, message)
        }

        @Test
        fun `Given map of station to price with null prices then return sms message populated with Not Available`() {
            val stationToPrice: Map<Station, Double?> = mapOf(
                SEVEN_ELEVEN to null,
                LIBERTY to null,
                COSTCO to null
            )

            val message = makeSmsMessage(stationToPrice)

            val expectedMessage = "$SEVEN_ELEVEN   N/A      \n$LIBERTY    N/A      \n$COSTCO     N/A      "
            assertEquals(expectedMessage, message)
        }
    }
}