package com.petsvote.filter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Breed
import com.petsvote.room.City
import com.petsvote.room.Country
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SelectCountryViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val TAG = SelectCountryViewModel::class.java.name

    private val _uiState = MutableStateFlow(listOf<Country>())
    val uiState: StateFlow<List<Country>> = _uiState

    fun getCountry(){

        viewModelScope.launch (Dispatchers.IO){
            var res = roomRepository.getCounties()
            if(!res.isNullOrEmpty()) _uiState.value = res
        }
    }

    fun saveLocation(country: Country){
        viewModelScope.launch (Dispatchers.IO) {
            //roomRepository.saveLocation()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val networkRepository: Provider<NetworkRepository>,
        private val roomRepository: Provider<RoomRepository>
    )
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SelectCountryViewModel::class.java)
            return SelectCountryViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }
    }
}