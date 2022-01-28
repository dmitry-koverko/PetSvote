package com.petsvote.register

import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Register
import com.petsvote.room.RoomRepository
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

    fun getCurrensies(code: String){
        viewModelScope.launch (Dispatchers.IO){
            var response: Register? = networkRepository.register(code)
            response?.let {
                roomRepository.saveRegister(it.bearer, it.user.id, it.user.first_name,
                                            it.user.last_name, it.user.avatar, it.user.first_vote)

                roomRepository.saveLocation(
                    it.user.location?.city_id, it.user.location?.country_id,
                    it.user.location?.country,it.user.location?.city)

                _uiState.value = true
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
}