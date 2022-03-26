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

    var isLoading = MutableStateFlow(false)
    var instagramUserName = MutableStateFlow("")

    var isLoadingAddPet = MutableStateFlow(false)
    var saveResult = MutableStateFlow("")

    fun getUserName(id: Long) {
        isLoading.value = true
        viewModelScope.launch {
            var res = networkRepository.getUsernameById(id)
            res?.let {
                instagramUserName.value = it
            }
            isLoading.value = false
        }
    }

    fun saveUserPet(photos:List<MultipartBody.Part>, user: AddPetFragment.CreatePetUi){
        viewModelScope.launch (Dispatchers.IO){
            isLoadingAddPet.value = true
            networkRepository.addPet(
                photos,
                user.birthDay,
                user.name,
                user.breedId.toString(),
                user.sex,
                user.kind
            )
            isLoadingAddPet.value = false
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