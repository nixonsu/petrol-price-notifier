package com.nixonsu.models

data class ElevenSevenResponse(val updated: Int, val regions: List<PriceForRegion>)

data class PriceForRegion(val region: Region, val prices: List<Entry>)
