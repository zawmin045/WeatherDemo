package com.example.weatherdemo.model

data class CurrentUnits(
    val interval: String,
    val is_day: String,
    val temperature_2m: String,
    val time: String,
    val wind_direction_10m: String,
    val wind_gusts_10m: String,
    val wind_speed_10m: String
)