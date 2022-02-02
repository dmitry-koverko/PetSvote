package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class Cities(
    val cities: List<City>,
    val lang: String,
    val total_count: Int
)

@Serializable
data class City(
    val id: Int,
    val important: Int?,
    val country_id: Int,
    val title: String,
    val region: String?,
    val area: String?,
    val region_id: Int?
)