package com.example.weatherdemo.model

data class Current(
    val interval: Int,
    val is_day: Int,
    val temperature_2m: Double,
    val time: String,
    val wind_direction_10m: Int,
    val wind_gusts_10m: Double,
    val wind_speed_10m: Double
)