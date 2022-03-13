package com.petsvote.pet.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.PetDatails
import com.petsvote.api.entity.User
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Provider

class PetInfoViewModel (
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    var uiPet = MutableStateFlow(Pet("",-1,"",-1,"",
        -1,"",-1,"","","",-1,
        -1,-1,"","",-1,
        -1,-1, listOf()))

    var uiPetDetails = MutableStateFlow(PetDatails("", "", "", -1,
        -1, -1, -1, -1, -1,
        -1, -1, -1, -1))

    fun getPetInfo(petId: Int){
        viewModelScope.launch (Dispatchers.IO){
           var res =  networkRepository.findPet(petId)
            res?.pet?.let {
                getPetDetails(res.pet.city_id, res.pet.country_id, res.pet.id, res.pet.user_id)
                uiPet.value = it
            }
        }
    }

    fun getPetDetails(city_id: Int?, country_id: Int?, id: Int?, user_id: Int?){

        viewModelScope.launch (Dispatchers.IO){
            var res =  networkRepository.petDetails(city_id, country_id, id, user_id)
            res?.let {
                uiPetDetails.value = it
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
            require(modelClass == PetInfoViewModel::class.java)
            return PetInfoViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }
}