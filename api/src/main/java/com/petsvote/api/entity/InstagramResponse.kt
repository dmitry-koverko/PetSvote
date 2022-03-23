package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class InstagramResponse(
    val user: UserInstagram,
    val status: String
)
