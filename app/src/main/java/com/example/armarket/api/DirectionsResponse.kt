package com.example.armarket.api

data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val legs: List<Leg>
)

data class Leg(
    val distance: Distance
)

data class Distance(
    val text: String,
    val value: Int // Distancia en metros
)
