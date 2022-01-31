package com.petsvote.room

import android.app.Application

class RoomRepository(private val application: Application): RRepository {

    private var userDao: UserDao = UserDatabase.getDatabase(application).userDao()
    private var locationDao: LocationDao = UserDatabase.getDatabase(application).locationDao()
    private var breedDao: BreedsDao = UserDatabase.getDatabase(application).breedDao()
    private var countryInfoDao: CountryInfoDao = UserDatabase.getDatabase(application).countryInfoDao()
    override suspend fun saveRegister(
        bearer: String?,
        id: Int?,
        first_name: String?,
        last_name: String?,
        avatar: String?,
        first_vote: Int?
    ) {
        var userInfo = UserInfo(id, bearer, first_name, false, last_name, avatar,
            first_vote, false, 0, 0)
        userDao.insert(userInfo)
    }

    override suspend fun saveLocation(
        city_id: Int?,
        country_id: Int?,
        country: String?,
        city: String?
    ) {
       var location = Location(city_id, country_id, country, city)
        locationDao.insert(location)
    }

    override suspend fun saveBreed(breed: Breed) {
        breedDao.insert(breed)
    }

    override suspend fun deleteBreeds() {
        breedDao.deleteAll()
    }

    override suspend fun saveCountries(info: CountryInfo) {
        countryInfoDao.insert(info)
    }

    override suspend fun getCounties(): List<Country> {
        return countryInfoDao.getCountryInfo().countries
    }


}