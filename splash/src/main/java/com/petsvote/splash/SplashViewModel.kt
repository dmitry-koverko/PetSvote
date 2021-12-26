package com.petsvote.splash

import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SplashViewModel(
    private val networkRepository: NetworkRepository
    //private val roomRepository: RoomRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashResult(false, false))
    val uiState: StateFlow<SplashResult> = _uiState
    private var result = SplashResult(false, false)

    fun getConfig(){

        viewModelScope.launch (Dispatchers.IO){
            result.globalConfig = networkRepository.getConfig()
            _uiState.value = result
        }

        viewModelScope.launch (Dispatchers.IO){
            result.localizationData = networkRepository.getLocalize()
            _uiState.value = result
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val networkRepository: Provider<NetworkRepository>
        //private val roomRepository: Provider<RoomRepository>
    )
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            return SplashViewModel(networkRepository.get()
                //roomRepository.get()
            ) as T
        }

    }
}

data class SplashResult(
    var globalConfig: Boolean,
    var localizationData: Boolean
)