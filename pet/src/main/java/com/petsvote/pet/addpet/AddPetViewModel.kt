package com.petsvote.pet.addpet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.User
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Provider

class AddPetViewModel (
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {


    fun saveUserPet(photos:List<MultipartBody.Part>){
        viewModelScope.launch (Dispatchers.IO){
            networkRepository.addPet(photos, "2022-02-13 23:00:07 +0000", 3336320, "Pet from Android", "1", "FEMALE", "cat")
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val networkRepository: Provider<NetworkRepository>,
        private val roomRepository: Provider<RoomRepository>
    )
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AddPetViewModel::class.java)
            return AddPetViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }
}