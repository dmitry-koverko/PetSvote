package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class FindPet(
    val pet: Pet
)