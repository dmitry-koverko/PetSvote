package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class GlobalConfig(
    val purchases: List<Purchase>,
    val breeds_versions: List<BreedsVersion>,
    val locals_versions: List<BreedsVersion>,
    val timezone: String,
    val count_votes: Int,
    val payments: Int,
    val pet_query_limit: Int,
    val append_pet_list_limit: Int,
    val append_rating_list_limit: Int
)

@Serializable
data class Purchase(
    val system_name: String
)

@Serializable
data class BreedsVersion(
    val code: String,
    val version: String,
)