package com.petsvote.rating

import android.util.Log
import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.PetRating
import com.petsvote.data.FilterPetsObject
import com.petsvote.data.UserInfo
import com.petsvote.room.Breed
import com.petsvote.room.Location
import com.petsvote.room.RoomRepository
import com.petsvote.room.UserPets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

    val uiStateUserPets =  MutableStateFlow(listOf<PetRating>())
    val uiStateTopPets =  MutableStateFlow(listOf<PetRating>())

    var uiLocation = MutableStateFlow(Location())

    fun getRating(offset: Int, filterRating: RatingFragment.FilterRating){
        viewModelScope.launch (Dispatchers.IO){
            Log.d(TAG, "getRating() listSize = ${filter.offset}")
            val res = networkRepository.getRating(offset, null, null, filterRating.type,
                filterRating.sex, filterRating.cityId, filterRating.countryId, filterRating.ageBetween,
                filterRating.breedId)
            res?.let {
                res.pets?.let {
                    _uiState.value = it
                    filter.offset = filter.offset?.plus(50);
                }
            }
        }
    }
    fun getRatingToTop(offset: Int, limit: Int?, filterRating: RatingFragment.FilterRating){
        viewModelScope.launch (Dispatchers.IO){
            val res = networkRepository.getRating(offset, null, limit, filterRating.type,
                filterRating.sex, filterRating.cityId, filterRating.countryId, filterRating.ageBetween,
                filterRating.breedId)
            res?.let {
                res.pets?.let {
                    uiStateTopPets.value = it
                }
            }
        }
    }

    fun getRatingMyPet(petId: Int, filterRating: RatingFragment.FilterRating){
        uiStateUserPets.value = listOf()
        viewModelScope.launch (Dispatchers.IO){
            val res = networkRepository.getRating(0, petId, null,
                filterRating.type,
                filterRating.sex, filterRating.cityId, filterRating.countryId, filterRating.ageBetween,
                filterRating.breedId)
            res?.let {
                res.pets?.let {
                    uiStateUserPets.value = it
                }
            }
        }
    }

    fun getUserInfo(){
        viewModelScope.launch (Dispatchers.IO){
            roomRepository.getCurrentUser().collect {
               Log.d(TAG, "userPets = ${it.toString()}")
               if(it != null){
                   it.pet.let { pets ->
                       _uiStatePets.value = pets
                   }
                   it.location?.let { location ->
                       uiLocation.value = location
                   }
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
    var offset: Int?
)
