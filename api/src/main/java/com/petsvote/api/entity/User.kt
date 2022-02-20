package com.petsvote.api.entity

import android.graphics.Bitmap
import kotlinx.serialization.Serializable

@Serializable
data class User(
    //val is_blocked: Boolean,
    val location: Location? = null,
    val pets: List<UserPets>? = listOf(),
    val id: Int? = -1,
    //val bearer: String,
    var first_name: String? = "",
    //val has_blocked: Boolean,
    var last_name: String? = "",
    val avatar: String? = "",
    val first_vote: Int? = -1,
    //val has_paid_votes: Boolean,
    //val notify_status: Int,
    val official: Int? = 0
)


@Serializable
data class Location(
    var city_id: Int,
    var country_id: Int,
    var country: String,
    var city: String
)

@Serializable
data class UserPets(
    val id: Int? = null,
    val pet_id: Int? = null,
    val name: String? = "",
    val first_name: String? = "",
    val last_name: String? = "",
    val avatar: String? = "",
    val official: Int? = -1,
    val global_range: Int? = -1,
    val country_range: Int? = -1,
    val city_range: Int? = -1,
    val global_score: Int? = -1,
    val global_dynamic: Int? = -1,
    val country_dynamic: Int? = -1,
    val city_dynamic: Int? = -1,
    val mark_dynamic: Int? = -1,
    val has_paid_votes: Int? = -1,
    val photos: List<Photo>? = listOf()
)
