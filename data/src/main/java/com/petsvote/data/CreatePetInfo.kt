package com.petsvote.data

import com.petsvote.room.Breed
import kotlinx.coroutines.flow.MutableStateFlow

object CreatePetInfo {

    var kind = MutableStateFlow(Kinds(-1, "", false, "", 0))
    var breed = MutableStateFlow(FilterBreed(-1, ""))

}