package com.petsvote.splash

import android.util.Log
import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Country
import com.petsvote.room.CountryInfo
import com.petsvote.room.RoomRepository
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

    fun getConfig(){

        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")


        viewModelScope.launch (Dispatchers.IO){


            val currentDateAndTime: String = simpleDateFormat.format(Date())
            Log.d(TAG, "START = $currentDateAndTime")

            var config  = networkRepository.getConfig()
            config?.let {
                result += 1
                _uiState.value = result
            }


        }

        viewModelScope.launch (Dispatchers.IO){
            var result = networkRepository.getCurrentUser()
            result?.let {
                Log.d(TAG, "userListPets = ${it.pets.toString()}")
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
                var countryInfo = CountryInfo(0,"ru", listOf(), dList as List<com.petsvote.room.Country>)
                roomRepository.saveCountries(countryInfo)
                val currentDateAndTimeF: String = simpleDateFormat.format(Date())
                Log.d(TAG, "FINISH = $currentDateAndTimeF")
            }
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
}