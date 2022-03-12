package com.petsvote.pet.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.PetDatails
import com.petsvote.api.entity.PetRating
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import kotlin.random.Random

class SearchPetViewModel (
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val _uiPet = MutableStateFlow<Pet?>(
        Pet("",-1,"",-1,"",
            -1,"",-1,"","","",-1,
            -1,-1,"","",-1,
            -1,-1, listOf())
    )
    var uiPet: MutableStateFlow<Pet?> = _uiPet

    fun getPetInfo(petId: Int){
        viewModelScope.launch (Dispatchers.IO){
            val randomValue = Random.nextInt(0, 100)
            _uiPet.value = null
            var res =  networkRepository.findPet(petId)
            if(res != null){
                _uiPet.value = res
            }else {
                _uiPet.value = Pet("",-2,"",-1,"",
                    -1,"",-1,"","","",-1,
                    -1,-1,"","",-1,
                    -1,randomValue, listOf())
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
            require(modelClass == SearchPetViewModel::class.java)
            return SearchPetViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }
}