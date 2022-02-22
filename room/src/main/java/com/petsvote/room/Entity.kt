package com.petsvote.room

import androidx.room.*
import com.petsvote.room.converters.*
import org.jetbrains.annotations.NotNull

@Entity
data class UserInfo(
    @PrimaryKey(autoGenerate = false)
    var id: Int? = 1,
    var bearer: String?  =null,
    var first_name: String?  =null,
    var has_blocked: Boolean?  =null,
    var last_name: String?  =null,
    var avatar: String?  =null,
    var first_vote: Int?  =null,
    var has_paid_votes: Boolean?  =null,
    var notify_status: Int?  =null,
    var official: Int? =null,

    @NotNull
    @TypeConverters(PetConverter::class)
    var pet: List<UserPets> = listOf(),

    @TypeConverters(LocationConverter::class)
    var location: Location? =null,
)

@Entity
data class Location(
    @PrimaryKey
    var city_id: Int? = null,
    val country_id: Int? = null,
    val country: String? = null,
    val city: String? = null
)

@Entity
data class UserPets(

    @PrimaryKey(autoGenerate = true)
    var idPet: Int? = 1,

    @Ignore
    val id: Int? = 1,

    @Ignore
    val name: String? = "",
    @Ignore
    var pets_id: Int? = 1,
    @Ignore
    val global_range: Int? =null,
    @Ignore
    val country_range: Int? =null,
    @Ignore
    val city_range: Int? =null,
    @Ignore
    val global_score: Int? =null,
    @Ignore
    val global_dynamic: Int? =null,
    @Ignore
    val country_dynamic: Int? =null,
    @Ignore
    val city_dynamic: Int? =null,
    @Ignore
    val mark_dynamic: Int? =null,
    @Ignore
    val has_paid_votes: Int? =null,

    @NotNull
    @Ignore
    @TypeConverters(PhotoConverter::class)
    var photos: List<Photo> = listOf()
)

@Entity
data class Photo(
    val id: Int,
    val num: Int,
    val url: String
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
