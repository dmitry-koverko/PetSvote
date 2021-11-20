package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class Localize(
    val id: Int,
    val lang: String,
    val version: String,
    val data: String
)