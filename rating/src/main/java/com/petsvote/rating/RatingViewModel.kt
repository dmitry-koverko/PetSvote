package com.petsvote.rating

import android.util.Log
import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.PetRating
import com.petsvote.api.entity.UserPets
import com.petsvote.data.UserInfo
import com.petsvote.room.Breed
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class RatingViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val TAG = RatingViewModel::class.java.name

    private val _uiState = MutableStateFlow(listOf<PetRating>())
    val uiState: StateFlow<List<PetRating>> = _uiState
    private var result = 0

    var filter = FilterPet(0)


    private val _uiStatePets = MutableStateFlow(listOf<UserPets>())
    val uiStatePets: StateFlow<List<UserPets>> = _uiStatePets

    fun getRating(){

        viewModelScope.launch (Dispatchers.IO){
            Log.d(TAG, "getRating() listSize = ${filter.offset}")
            val res = networkRepository.getRating(filter.offset)
            res?.let {
                _uiState.value = res.pets
                filter.offset =+ 50;
            }
        }

    }

    fun getUserInfo(){
        viewModelScope.launch (Dispatchers.IO){
            val res = networkRepository.getCurrentUser()
            res?.let {
                 it.pets?.let {
                     _uiStatePets.value
                 }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val networkRepository: Provider<NetworkRepository>,
        private val roomRepository: Provider<RoomRepository>
    )
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == RatingViewModel::class.java)
            return RatingViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }

    fun addListSizeFilter(size: Int){
        filter.offset = size
    }
}

data class FilterPet(
    var offset: Int
)
