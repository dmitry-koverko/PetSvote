package com.petsvote.register

import androidx.lifecycle.*
import com.petsvote.api.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class RegisterViewModel(
    private val networkRepository: NetworkRepository
    //private val roomRepository: RoomRepository,
) : ViewModel() {

//    private val _uiState = MutableStateFlow(listOf<Currency>())
//    val uiState: StateFlow<List<Currency>> = _uiState


    fun getCurrensies(){

        viewModelScope.launch (Dispatchers.IO){
//            var list = mutableListOf<Currency>()
//            list.add(Currency(0, null, null, null))
//            for (i in 1..10){
//                list.add(Currency(i, "code$i", (i*100).toDouble(),
//                    i % 2 == 0
//                ))
//            }
//            _uiState.value = list
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val networkRepository: Provider<NetworkRepository>
        //private val roomRepository: Provider<RoomRepository>
    )
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == RegisterViewModel::class.java)
            return RegisterViewModel(networkRepository.get()
                //roomRepository.get()
                ) as T
        }

    }
}