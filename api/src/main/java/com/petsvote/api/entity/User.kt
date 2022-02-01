package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    //val is_blocked: Boolean,
    val location: Location?,
    val pets: List<UserPets>?,
    val id: Int?,
    //val bearer: String,
    var first_name: String?,
    //val has_blocked: Boolean,
    var last_name: String?,
    val avatar: String?,
    val first_vote: Int?,
    //val has_paid_votes: Boolean,
    //val notify_status: Int,
    val official: Int?
)


@Serializable
data class Location(
    val city_id: Int,
    var country_id: Int,
    var country: String,
    val city: String
)

@Serializable
data class UserPets(
    val first_name: String,
    val last_name: String,
    val avatar: String,
    val official: Int,
    val global_range: Int,
    val country_range: Int,
    val city_range: Int,
    val global_score: Int,
    val global_dynamic: Int,
    val country_dynamic: Int,
    val city_dynamic: Int,
    val mark_dynamic: Int,
    val has_paid_votes: Int
)
