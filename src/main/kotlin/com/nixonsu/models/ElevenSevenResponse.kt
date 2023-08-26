package com.nixonsu.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ElevenSevenResponse(
    val updated: Long,
    val regions: List<Region>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Region(
    val region: String,
    val prices: List<Price>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Price(
    val name: String,
    val type: String,
    val price: Double?,
    val suburb: String,
    val state: String,
    val postcode: String,
    val lat: Double,
    val lng: Double
)
