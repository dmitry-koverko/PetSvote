package com.petsvote.room

import androidx.room.*
import com.petsvote.room.converters.CityConverter
import com.petsvote.room.converters.CountryConverter

@Entity
data class UserInfo(
    @PrimaryKey
    var id: Int?,
    var bearer: String? ,
    var first_name: String? ,
    var has_blocked: Boolean? ,
    var last_name: String? ,
    var avatar: String? ,
    var first_vote: Int? ,
    var has_paid_votes: Boolean? ,
    var notify_status: Int? ,
    var official: Int?
)

@Entity
data class Location(
    @PrimaryKey
    val city_id: Int?,
    val country_id: Int?,
    val country: String?,
    val city: String?
)

@Entity
data class UserPets(
    @PrimaryKey(autoGenerate = true)
    val pets_id: Int,
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

@Entity
data class Breed(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val lang: String,
    val type: String,
    val id_breed: Int,
    val title: String
)

@Entity
data class CountryInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val language: String,
    @TypeConverters(CityConverter::class)
    val cities: List<City>?,

    @TypeConverters(CountryConverter::class)
    val countries: List<Country>
)

@Entity
data class City(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val important: Int,
    val country_id: Int,
    val title: String,
    val region: String,
    val area: String,
    val region_id: Int
)

@Entity
data class Country(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String
)
