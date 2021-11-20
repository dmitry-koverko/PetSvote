package com.petsvote.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class Pets(
    val pets: List<Pet>,
    val lang: String,
    val total_count: Int,
    val offset: Int
)

@Serializable
data class Pet(
    val  temp_type: String,
    val id : Int,
    val create_date: String,
    val pet_id: Int,
    val bdate: String,
    val user_id: Int,
    val name: String,
    val breed_id: Int,
    val sex: String,
    val type: String,
    val inst: String,
    val count_paid_votes: Int,
    val last_paid_rating_sum: Int,
    val has_paid_votes: Int,
    val country_name: String,
    val city_name: String,
    val country_id: Int,
    val city_id: Int,
    val card_type: Int,
    val photos: List<Photo>,
)

@Serializable
data class Photo(
    val id: Int,
    val num: Int,
    val url: String
)