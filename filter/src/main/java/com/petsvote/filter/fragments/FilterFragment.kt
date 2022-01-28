package com.petsvote.filter.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petsvote.api.entity.Breed
import com.petsvote.data.FilterPetsObject
import com.petsvote.data.FilterSex
import com.petsvote.data.Kinds
import com.petsvote.filter.R
import com.petsvote.filter.databinding.DialogRatingPetFilterBinding
import com.petsvote.ui.BesieTabLayoutSelectedListener
import com.petsvote.ui.BesieTabSelected
import kotlinx.coroutines.flow.collect
import java.io.Serializable

class FilterFragment: Fragment(R.layout.dialog_rating_pet_filter), BesieTabLayoutSelectedListener {

    private val TAG = FilterFragment::class.java.name
    private var mFragmentListener: FilterFragmentListener? = null

    private var minValue = 0
        set(value) {
            if(value >= 0){
                field = value
                FilterPetsObject.minValue.value = value
                updateMinText()
            }
        }

    private var maxValue = 200
        set(value) {
            if(value <= 200){
                field = value
                FilterPetsObject.maxValue.value = value
                updateMaxText()
            }
        }

    private var currentTab = BesieTabSelected.ALL

    var filterKinds: MutableList<Kinds>? = mutableListOf<Kinds>()
        set(value) {
            field = value
            if(!value.isNullOrEmpty()) updateKindsText()
        }
    var breedId: Int = 0
        set(value) {
            field = value
            updateBreedsText()
        }

    var breedName: String = ""
    var kindClickable = false

    private fun updateBreedsText() {
        binding.breeds.text = when(breedId){
            0 -> getString(R.string.all_breeds)
            1 -> getString(R.string.no_breeds)
            else -> breedName
        }
    }

    private fun updateKindsText() {
        var str = ""
        for (i in 0 until filterKinds!!.size){
            str += "${filterKinds!![i].name}"
            if(i != filterKinds!!.size -1) str += ", "
        }
        maxValue = filterKinds!!.maxByOrNull { it.age }?.age!!
        minValue = 0
        binding.kids.text = str
    }

    private lateinit var binding: DialogRatingPetFilterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogRatingPetFilterBinding.bind(view)

        binding.kidsText.setOnClickListener {
            binding.containerKids.isPressed = true
            binding.containerKids.performClick()
        }

        binding.kids.setOnClickListener {
            binding.containerKids.isPressed = true
            binding.containerKids.performClick()
        }

        binding.breedsText.setOnClickListener {
            binding.containerBreeds.isPressed = true
            binding.containerBreeds.performClick()
        }

        binding.breeds.setOnClickListener {
            binding.containerBreeds.isPressed = true
            binding.containerBreeds.performClick()
        }

        binding.containerKids.setOnClickListener {
            var bundle = Bundle()
            if(!filterKinds.isNullOrEmpty())
                bundle.putSerializable("kinds", filterKinds as Serializable)
            findNavController().navigate(R.id.action_filterFragment_to_selectKidsFragment, bundle)
            setSex()
        }
        binding.containerBreeds.setOnClickListener {
            if(kindClickable){
                findNavController().navigate(R.id.action_filterFragment_to_selectBreedsFragment)
            }
            setSex()
        }

        binding.imgLeftAgeM.setOnClickListener {
            binding.blLeft1.animRipple()
            minValue --
        }

        binding.imgLeftAgeP.setOnClickListener {
            binding.blLeft2.animRipple()
            minValue ++
        }

        binding.imgRightAgeM.setOnClickListener {
            binding.blRight1.animRipple()
            maxValue --
        }

        binding.imgRightAgeP.setOnClickListener {
            binding.blRight2.animRipple()
            maxValue ++
        }

        lifecycleScope.launchWhenResumed {
            getListKinds()
        }
        lifecycleScope.launchWhenResumed {
            getBreed()
        }
        lifecycleScope.launchWhenResumed {
            getSex()
        }

        binding.tabs.setTabSelectedListener(this)
        binding.applyRipple.setOnClickListener {
            setSex()
            FilterPetsObject.show.value = 2
        }

    }

    private suspend fun getSex() {
        FilterPetsObject.sex.collect {
            Log.d(TAG, "${it.type}")
            when(it.type){
                1 -> binding.tabs.initCountryTabs()
                2 -> binding.tabs.initWorldTab()
            }
        }
    }

    private suspend fun getBreed() {
        FilterPetsObject.breed.collect {valueBreed ->
            if(valueBreed != null){
                if (valueBreed.title.isNotEmpty()) {
                    breedName = valueBreed.title
                }
                if(valueBreed.id == -1) breedName = getString(R.string.no_breeds)
                else if(valueBreed.id == 0) breedName = getString(R.string.all_breeds)

                breedId = valueBreed.id
            }
        }
    }

    private suspend fun getListKinds() {
        FilterPetsObject.listKinds.collect { list ->
            if(!list.isNullOrEmpty()){
                var list: MutableList<Kinds> = list as MutableList<Kinds>
                if(!list.isNullOrEmpty()) {
                    filterKinds = list
                    kindClickable = list.size == 1
                }
                else {
                    binding.kids.text = context?.resources?.getString(R.string.all_kinds)
                    kindClickable = false
                }
                breedId = 0
            }
        }

    }

    private fun updateMinText() {
        if(minValue <= maxValue){
            binding.sfMinValue.text = minValue.toString()
        }
    }

    private fun updateMaxText() {
        if(maxValue >= minValue){
            binding.sfMaxValue.text = maxValue.toString()
        }
    }

    data class FilterObject(
        var kinds: List<Kinds>?,
        var breedId: Int,
        var breedName: String
    )

    override fun selected(tab: BesieTabSelected) {
        currentTab = tab
    }
    private fun setSex(){
        var type = when(currentTab){
            BesieTabSelected.ALL -> 0
            BesieTabSelected.MAN -> 1
            BesieTabSelected.GIRLS -> 2
            else -> 0
        }
        FilterPetsObject._sex.value = FilterSex((type))
    }


    fun setOnFilterFragmentListener(filterFragmentListener: FilterFragmentListener){
        mFragmentListener = filterFragmentListener
    }

    interface FilterFragmentListener{
        fun onSelect()
    }
}