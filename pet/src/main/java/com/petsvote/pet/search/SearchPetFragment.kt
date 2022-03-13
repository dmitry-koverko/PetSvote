package com.petsvote.pet.search

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.petsvote.pet.R
import com.petsvote.pet.databinding.FragmentPetInfoBinding
import com.petsvote.pet.databinding.FragmentSearchPetBinding
import com.petsvote.pet.di.PetComponentViewModel
import com.petsvote.pet.info.PetInfoFragment
import com.petsvote.pet.info.PetInfoViewModel
import com.petsvote.ui.*
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SearchPetFragment: Fragment(R.layout.fragment_search_pet) {

    private val TAG = SearchPetFragment::class.java.name

    private lateinit var binding: FragmentSearchPetBinding
    @Inject
    internal lateinit var searchViewModelFactory: Lazy<SearchPetViewModel.Factory>

    private val petCViewModel: PetComponentViewModel by viewModels()
    private val viewModel: SearchPetViewModel by viewModels {
        searchViewModelFactory.get()
    }

    private var text = ""
    private var isShowKeyboard = true

    private var stars: List<View>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchPetBinding.bind(view)

        binding.searchBar.editable = false
        stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)

        binding.searchBar.textHint = "0000 0000"

        binding.keyboard.setOnKeyboardListener(object : BesieKeyboard.BesieKeyboardListener{
            override fun click(value: Int) {
                if(value != -1){
                    text = "$text$value"
                }else {
                    if(text.isNotEmpty()){
                       text = text.substring(0, text.length -1 )
                    }
                }
                binding.findContainer.dotColor =
                    if(text.length == 8 ) ContextCompat.getColor(context!!, R.color.ui_primary)
                    else ContextCompat.getColor(context!!, R.color.disable_btn)

                binding.searchBar.textSearch = text
            }

        })

        binding.searchBar.setOnTextSearchBar(object : SearchBar.OnTextSearchBar{
            override fun onText(txt: String) {
                text = txt
            }
            override fun onClear() {
                text = ""
                binding.searchBar.textHint = "0000 0000"
                binding.findContainer.dotColor =
                    ContextCompat.getColor(context!!, R.color.disable_btn)
                if(!isShowKeyboard) showKeyboard()
            }

        })

        binding.findContainer.setOnClickListener {
            if(text.isNotEmpty() && text.length >=8){
                var rInt: Int? = text.toIntOrNull()
                rInt?.let {
                    viewModel.getPetInfo(it)
                }
            }
        }

       lifecycleScope.launchWhenStarted {
           viewModel.uiPet.collect { findPet ->
               findPet?.let {
                   var pet = findPet.pet
                   when(pet.id){
                       -2 -> {
                           showStateNoFind()
                       }
                       -1 -> {}
                       else -> {
                           hideKeyboard()
                           binding.petContainer.visibility = View.VISIBLE
                           binding.containerRating.visibility = View.VISIBLE
                           binding.image.loadImage(pet.photos.get(0).url)
                           var sexSimbol = when(pet.sex){
                               "MALE" -> "♂"
                               "FEMALE" -> "♀"
                               else -> ""
                           }
                           binding.name.text =
                               "${pet.name}, ${context?.getMonthOnYear(pet.bdate)} $sexSimbol"
                           binding.location.text = "${pet.city_name}, ${pet.country_name}"
                           if(findPet.vote != null){
                               binding.voteStatusTrue.visibility = View.VISIBLE
                               binding.voteContainer.visibility = View.GONE
                               binding.voteBar.visibility = View.VISIBLE
                               startAnimaVote(findPet.vote!! -1)

                           }else {
                               binding.voteStatusTrue.visibility = View.GONE
                               binding.voteContainer.visibility = View.VISIBLE
                               binding.voteBar.visibility = View.GONE
                           }
                       }
                   }
               }
           }
       }
    }

    private fun showStateNoFind() {
        hideKeyboard()
        binding.noFindContainer.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        binding.keyboard.visibility = View.GONE
        binding.findContainer.visibility = View.GONE
        isShowKeyboard = false
    }

    private fun showKeyboard(){
        binding.keyboard.visibility = View.VISIBLE
        binding.findContainer.visibility = View.VISIBLE
        isShowKeyboard = true
        binding.noFindContainer.visibility = View.GONE
        binding.petContainer.visibility = View.GONE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        petCViewModel.petComponent.injectSearch(this)
    }

    private fun startAnimaVote(rate: Int){
        for(starIndex in 0..rate){
            (stars?.get(starIndex) as Star).visibility = View.VISIBLE
            (stars?.get(starIndex) as Star).animRipple(true)
        }
    }
}