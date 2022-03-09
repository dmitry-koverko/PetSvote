package com.petsvote.splash

import android.util.Log
import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Country
import com.petsvote.api.entity.UserPets
import com.petsvote.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class SplashViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {
    private val TAG = SplashViewModel::class.java.name

    private val _uiState = MutableStateFlow(0)
    val uiState: StateFlow<Int> = _uiState
    private var result = 0

    fun getConfig(language: String){

        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        Log.d(TAG, "START = $currentDateAndTime")

//        viewModelScope.launch (Dispatchers.IO){
//
//
//
//            var config  = networkRepository.getConfig()
//            config?.let {
//                result += 1
//                _uiState.value = result
//            }
//        }

        viewModelScope.launch (Dispatchers.IO){
            var result = networkRepository.getCurrentUser()
            result?.let {
                var r = roomRepository.deleteUserInfo()
                Log.d(TAG, "userListPets = ${it.pets.toString()}")
                var userInfo = UserInfo()
                userInfo.avatar = it.avatar
                userInfo.first_name = it.first_name
                userInfo.last_name = it.last_name
                userInfo.location = Location(it.location?.country_id, it.location?.city_id,
                                    it.location?.country, it.location?.city)
                userInfo.pet = convertApitoRoom(it.pets)
                roomRepository.updateUser(userInfo)
            }
        }

        viewModelScope.launch (Dispatchers.IO){

            var res = networkRepository.getCountries()
            res?.let {
                _uiState.value = result
                var dList = mutableListOf<com.petsvote.room.Country>()
                for(country in it){
                    var dCountry = com.petsvote.room.Country(country.id, country.title)
                    dList.add(dCountry)
                }
                var countryInfo =
                    CountryInfo(0,language , listOf(), dList as List<com.petsvote.room.Country>)
                roomRepository.saveCountries(countryInfo)
                val currentDateAndTimeF: String = simpleDateFormat.format(Date())
                Log.d(TAG, "FINISH = $currentDateAndTimeF")
            }

            //TODO sad
//            var breedsList= networkRepository.getBreeds()
//            breedsList?.let {
//                roomRepository.deleteBreeds()
//                for(breeds in breedsList){
//
//                    for(breed in breeds.breeds){
//
//                        var breedRoom = Breed(0, breeds.lang, breeds.type, breed.id, breed.title)
//                        //roomRepository.saveBreed(breedRoom)
//                    }
//
//                }
//
//                result += 1
//                _uiState.value = result
//
//            }

        }

    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val networkRepository: Provider<NetworkRepository>,
        private val roomRepository: Provider<RoomRepository>
    )
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            return SplashViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }

    fun convertApitoRoom(pets: List<UserPets>?): List<com.petsvote.room.UserPets> {
        var list = mutableListOf<com.petsvote.room.UserPets>()
        if (pets != null) {
            for (i in pets){
                val list_photos = mutableListOf<Photo>()
                if(i.photos != null){
                    for (i in i.photos!!){
                        list_photos.add(Photo(i.id, i.num, i.url))
                    }
                }
                list.add(com.petsvote.room.UserPets(null, i.id, i.name,
                    i.pet_id, i.global_range, i.country_range, i.city_range, i.global_score,
                    i.global_dynamic, i.country_dynamic, i.city_dynamic, i.mark_dynamic,
                    i.has_paid_votes, list_photos
                ))
            }
        }
        return list
    }
}