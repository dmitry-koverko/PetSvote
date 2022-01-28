package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val bearer: String,
    val user: User,
    val is_new_user: Boolean
)