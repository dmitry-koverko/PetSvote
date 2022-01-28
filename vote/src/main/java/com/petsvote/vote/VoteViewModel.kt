package com.petsvote.vote

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

class VoteViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val TAG = VoteViewModel::class.java.name

    private val _uiState = MutableStateFlow(listOf<Pet>())
    val uiState: StateFlow<List<Pet>> = _uiState

    fun getRating(){

        viewModelScope.launch (Dispatchers.IO){
            val res = networkRepository.getPetsList()
            res?.let {
                _uiState.value = it
//                filter.offset =+ 50;
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
            require(modelClass == VoteViewModel::class.java)
            return VoteViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }
}