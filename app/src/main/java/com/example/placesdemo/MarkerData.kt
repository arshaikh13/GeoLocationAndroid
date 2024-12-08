package com.example.placesdemo.models

data class MarkerData(val lat: Double, val lng: Double) {
    fun toPair(): Pair<String, String> = "Lat: $lat, Lng: $lng" to ""
}
