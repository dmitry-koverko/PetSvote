package com.iqeon.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.User
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class UPViewModel (
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    var uiUser = MutableStateFlow(User(null,null, null, null,
        null, null, null, null))

    fun getUserInfo(){
        viewModelScope.launch (Dispatchers.IO) {
            var user = networkRepository.getCurrentUser()
            user?.let {
                uiUser.value = user
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
            require(modelClass == UPViewModel::class.java)
            return UPViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }
}