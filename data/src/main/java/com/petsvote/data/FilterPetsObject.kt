package com.petsvote.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FilterPetsObject {

    var _listKinds =  MutableStateFlow(listOf<Kinds>())
    var listKinds: StateFlow<List<Kinds>> = _listKinds

    var _breed = MutableStateFlow(FilterBreed(0, ""))
    var breed: StateFlow<FilterBreed> = _breed

    var _sex =  MutableStateFlow(FilterSex(0))
    var sex: StateFlow<FilterSex> = _sex

    var show: MutableStateFlow<Int> = MutableStateFlow(0)
    var minValue: MutableStateFlow<Int> = MutableStateFlow(0)
    var maxValue: MutableStateFlow<Int> = MutableStateFlow(200)

}

data class FilterBreed(
    var id: Int,
    var title: String
)

data class FilterSex(
    var type: Int
)