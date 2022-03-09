package com.petsvote.filter.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Breed
import com.petsvote.api.entity.Breeds
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.PetRating
import com.petsvote.data.UserInfo
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class SelectBreedsViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val TAG = SelectBreedsViewModel::class.java.name

    private val _uiState = MutableStateFlow(listOf<Breed>())
    val uiState: StateFlow<List<Breed>> = _uiState
    fun getBreeds(lang: String, type: String?){

        viewModelScope.launch (Dispatchers.IO){
            val  resRoom = roomRepository.getBreeds(lang, type)
            if(resRoom != null && resRoom.isNotEmpty()){
                var lisRoom = mutableListOf<Breed>()
                for(i in resRoom)
                    lisRoom.add(Breed(i.id_breed, i.title))
                _uiState.value = lisRoom
            }else {
                val res = networkRepository.getBreeds(type)
                res?.let {
                    var breed: Breeds? = it[0]
                    _uiState.value = breed?.breeds!!
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
            require(modelClass == SelectBreedsViewModel::class.java)
            return SelectBreedsViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }
    }
}
