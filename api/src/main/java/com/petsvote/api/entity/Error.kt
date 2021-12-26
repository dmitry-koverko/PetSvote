package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val status: String,
    val message: String
)