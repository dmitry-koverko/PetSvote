package com.petsvote.filter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.City
import com.petsvote.data.FilterUserInfo
import com.petsvote.room.Location
import com.petsvote.room.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SelectCityViewModel(
    private val networkRepository: NetworkRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val TAG = SelectCityViewModel::class.java.name

    private val _uiState = MutableStateFlow(listOf<City>())
    val uiState: StateFlow<List<City>> = _uiState

    private val _uiStateLocation = MutableStateFlow(Location(-1, -1, "", ""))
    val uiStateLocation: StateFlow<Location> = _uiStateLocation

    fun getLocation(){
        viewModelScope.launch (Dispatchers.IO){
            var res = roomRepository.getLocation()
            if(res.country_id != -1) {
                _uiStateLocation.value = res
                res.country_id?.let { getCities(it) }
            }
            else{
                var country = FilterUserInfo.country.value
                if(country.id != -1 && country.id != 0){
                    getCities(country.id)
                }
            }

        }

    }

    fun getCities(countryId: Int){
        viewModelScope.launch (Dispatchers.IO){
            var res = countryId.let { networkRepository.getCities(it) }
            if(!res.isNullOrEmpty()) _uiState.value = res
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val networkRepository: Provider<NetworkRepository>,
        private val roomRepository: Provider<RoomRepository>
    )
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == SelectCityViewModel::class.java)
            return SelectCityViewModel(networkRepository.get(),
                roomRepository.get()
            ) as T
        }
    }
}