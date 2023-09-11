package com.nixonsu.petrolpricenotifier.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.regex.Pattern

fun extractU91PriceFromHtml(petrolSpyHtmlResponse: String): Double? {
    val document = Jsoup.parse(petrolSpyHtmlResponse)
    val pricesList: Element? = document.getElementById("prices-list")

    if (pricesList != null) {
        for (price in pricesList.children()) {
            val pattern = Pattern.compile("Unleaded 91\\s+(\\d+\\.\\d+)")
            val matcher = pattern.matcher(price.text())

            if (matcher.find()) {
                return matcher.group(1).toDouble()
            }
        }
    }

    return null
}
