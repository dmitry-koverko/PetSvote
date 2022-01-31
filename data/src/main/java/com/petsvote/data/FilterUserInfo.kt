package com.petsvote.data

import com.petsvote.room.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FilterUserInfo {

    var country =  MutableStateFlow(listOf<Country>())

}
