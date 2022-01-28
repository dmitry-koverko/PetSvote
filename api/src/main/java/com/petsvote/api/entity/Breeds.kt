package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class Breeds(
    val lang: String,
    val version: String,
    val type: String,
    val breeds: List<Breed>
)

@Serializable
data class Breed(
    val id: Int,
    val title: String,
)