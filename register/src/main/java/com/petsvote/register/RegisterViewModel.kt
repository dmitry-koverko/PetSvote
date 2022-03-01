package com.petsvote.register

import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Register
import com.petsvote.api.entity.UserPets
import com.petsvote.data.DocumentsInfo
import com.petsvote.room.Location
import com.petsvote.room.Photo
import com.petsvote.room.RoomRepository
import com.petsvote.room.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class RegisterViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(false)
    val uiState: StateFlow<Boolean> = _uiState

    val uiStateTerms = MutableStateFlow<String>("")
    val uiStatePolicy = MutableStateFlow<String>("")

    fun getCurrensies(code: String){
        viewModelScope.launch (Dispatchers.IO){
            var response: Register? = networkRepository.register(code)
            response?.user?.let { it ->
                var userInfo = UserInfo()
                userInfo.avatar = it.avatar
                userInfo.first_name = it.first_name
                userInfo.last_name = it.last_name
                userInfo.location = Location(it.location?.country_id, it.location?.city_id,
                    it.location?.country, it.location?.city)
                userInfo.pet = convertApitoRoom(it.pets)
                roomRepository.updateUser(userInfo)
                _uiState.value = true
            }
        }
    }

    fun getTerms(lang: String){
        viewModelScope.launch (Dispatchers.IO){
            var term = networkRepository.getTerms(lang)
            term?.let {
                uiStateTerms.value = it.data
            }
        }
    }

    fun getPolicy(lang: String){
        viewModelScope.launch (Dispatchers.IO){
            var policy = networkRepository.getPolicy(lang)
            policy?.let {
                uiStatePolicy.value = it.data
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
            require(modelClass == RegisterViewModel::class.java)
            return RegisterViewModel(networkRepository.get(),
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