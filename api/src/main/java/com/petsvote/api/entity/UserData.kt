package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val avatar: String,
    val official: Int,
    val pets: List<UserPets>
)