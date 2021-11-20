package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class Countries(
    val lang: String,
    val countries: List<Country>,
)

@Serializable
data class Country(
    val id: Int,
    val title: String
)