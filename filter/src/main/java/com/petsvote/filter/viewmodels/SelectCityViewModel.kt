package com.petsvote.filter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkRepository
import com.petsvote.api.entity.Breed
import com.petsvote.data.FilterUserInfo
import com.petsvote.room.City
import com.petsvote.room.Country
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

    private val _uiState = MutableStateFlow(listOf<Country>())
    val uiState: StateFlow<List<Country>> = _uiState

    private val _uiStateLocation = MutableStateFlow(Location(-1, -1, "", ""))
    val uiStateLocation: StateFlow<Location> = _uiStateLocation

    fun getCountry(){
        viewModelScope.launch (Dispatchers.IO){
            var res = roomRepository.getCounties()
            if(!res.isNullOrEmpty()) _uiState.value = res
        }

        viewModelScope.launch (Dispatchers.IO){
//            var res = roomRepository.getLocation()
//            if(res.country_id != -1) _uiStateLocation.value = res
//            else{
//                var country = FilterUserInfo.country.value
//                if(country.id != -1 && country.id != 0){
//                    _uiStateLocation.value = Location(
//                        country.id, -1, country.title, ""
//                    )
//                }
//            }
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