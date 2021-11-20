package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class CityCountry(
    val city_id: Int,
    val country_id: Int,
    val country: String,
    val city: String
)