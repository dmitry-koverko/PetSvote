package com.petsvote.filter.fragments

import com.petsvote.filter.R
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Breed
import com.petsvote.api.entity.PetRating
import com.petsvote.data.FilterBreed
import com.petsvote.data.FilterPetsObject
import com.petsvote.filter.adapter.BreedsAdapter
import com.petsvote.filter.adapter.KindsAdapter
import com.petsvote.filter.databinding.FragmentSelectBeedsBinding
import com.petsvote.filter.di.FilterComponentViewModel
import com.petsvote.filter.fragments.MenuFilterFragment
import com.petsvote.filter.viewmodels.SelectBreedsViewModel
import com.petsvote.ui.SearchBar
import com.petsvote.ui.list.RecyclerViewLoadMoreScroll
import com.petsvote.ui.loadImage
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SelectBreedsFragment: Fragment(R.layout.fragment_select_beeds), BreedsAdapter.OnSelectedItem,
    SearchBar.OnTextSearchBar {

    @Inject
    internal lateinit var selectBreedsViewModelFactory: Lazy<SelectBreedsViewModel.Factory>

    private val selectBreedsComponentViewModel: FilterComponentViewModel by viewModels()
    private val selectBreedsViewModel: SelectBreedsViewModel by viewModels {
        selectBreedsViewModelFactory.get()
    }

    private var listKinds = mutableListOf<Breed>()
    private var adapter: BreedsAdapter? = null
    private var breedsAdapter = BreedsAdapter(listKinds)

    private lateinit var binding: FragmentSelectBeedsBinding

    private var breedsId = 0
    private var breedsName = ""

    private lateinit var allString: String
    private lateinit var noString: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectBeedsBinding.bind(view)
        selectBreedsViewModel.getBreeds()

        allString = getString(R.string.all_breeds)
        noString = getString(R.string.no_breeds)

        this.adapter = breedsAdapter
        binding.list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = breedsAdapter
        }

        breedsAdapter.setOnSelected(this)

        lifecycleScope.launchWhenStarted {
            selectBreedsViewModel.uiState.collect { uiState ->
                if(uiState.isNotEmpty()){
                    listKinds.addAll(uiState as MutableList<Breed>)
                    breedsAdapter.notifyDataSetChanged()

                    binding.progress.visibility = View.GONE
                    binding.nested.visibility = View.VISIBLE
                }
            }
        }


        binding.breedsAll.setOnClickListener {
            breedsName = allString
            binding.checkAll.visibility = View.VISIBLE
            binding.checkNo.visibility = View.GONE
            breedsId = 0
            breedsAdapter.setSelectedId(-1)
            setBreeds()
        }

        binding.breedsNo.setOnClickListener {
            breedsName = noString
            binding.checkAll.visibility = View.GONE
            binding.checkNo.visibility = View.VISIBLE
            breedsId = -1
            breedsAdapter.setSelectedId(-1)
            setBreeds()
        }

        binding.searchBar.setOnTextSearchBar(this)

        lifecycleScope.launchWhenResumed {
            getBreed()
        }
    }

    private suspend fun getBreed() {
        FilterPetsObject.breed.collect {
            var breed_id = it.id
            if(breed_id == -1) {
                binding.checkNo.visibility = View.VISIBLE
                binding.checkAll.visibility = View.GONE
            }else if(breed_id == 0){
                binding.checkNo.visibility = View.GONE
                binding.checkAll.visibility = View.VISIBLE
            }else {
                breedsAdapter.setSelectedId(breed_id)
                binding.checkAll.visibility = View.GONE
                binding.checkNo.visibility = View.GONE
            }
            breedsId = breed_id

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        selectBreedsComponentViewModel.filterComponent.injectSelectBreeds(this)
    }

    override fun select(breed: Breed) {
        breedsId = breed.id
        breedsName = breed.title
        setBreeds()
    }

    private fun setBreeds(){
        var breed = FilterBreed(breedsId, breedsName)
        FilterPetsObject._breed.value = breed
        findNavController().navigate(R.id.action_selectBreedsFragment_to_filterFragment)
    }

    override fun onText(text: String) {
        var lst = listKinds.filter { it.title.contains(text) }
        breedsAdapter.updateList(lst as MutableList<Breed>)
    }

    override fun onClear() {
        breedsAdapter.updateList(listKinds)
    }

}