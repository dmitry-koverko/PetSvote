package com.petsvote.data

import com.petsvote.room.City
import com.petsvote.room.Country
import kotlinx.coroutines.flow.MutableStateFlow


object FilterUserInfo {

    var country =  MutableStateFlow(Country(0,""))
    var city =
        MutableStateFlow(City(0,0, 0, "", "", "", 0))

}
