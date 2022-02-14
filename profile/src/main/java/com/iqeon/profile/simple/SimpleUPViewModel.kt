package com.iqeon.profile.simple

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.User
import com.petsvote.api.entity.UserPets
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Provider

class SimpleUPViewModel (
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    var uiUser = MutableStateFlow(listOf<UserPets>())

    fun getUserPets(){
        viewModelScope.launch (Dispatchers.IO) {
            var list = networkRepository.getCurrentUser()
            list?.pets?.let {
                uiUser.value = it
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
            require(modelClass == SimpleUPViewModel::class.java)
            return SimpleUPViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }

    }
}