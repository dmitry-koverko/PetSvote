package com.petsvote.filter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.petsvote.data.FilterUserInfo
import com.petsvote.filter.R
import com.petsvote.filter.adapter.CountryAdapter
import com.petsvote.filter.adapter.KindsAdapter
import com.petsvote.filter.databinding.FragmentSelectCountryBinding
import com.petsvote.filter.di.FilterComponentViewModel
import com.petsvote.filter.viewmodels.SelectCountryViewModel
import com.petsvote.room.Country
import com.petsvote.room.Location
import com.petsvote.ui.SearchBar
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SelectCountryFragment : Fragment(R.layout.fragment_select_country),
    SearchBar.OnTextSearchBar, CountryAdapter.OnSelectedItem {

    @Inject
    internal lateinit var sCountryVMFactory: Lazy<SelectCountryViewModel.Factory>

    private val filterComponentViewModel: FilterComponentViewModel by viewModels()
    private val viewModel: SelectCountryViewModel by viewModels {
        sCountryVMFactory.get()
    }

    private var listCountry = mutableListOf<Country>()
    private var adapter: CountryAdapter? = null
    var countryAdapter = CountryAdapter(listCountry)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSelectCountryBinding.bind(view)

        this.adapter = countryAdapter
        countryAdapter.setOnSelected(this)
        binding.listCountry.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = countryAdapter
        }

        binding.searchBar.setOnTextSearchBar(this)

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                listCountry.addAll(it)
                countryAdapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiStateLocation.collect { value: Location ->
                if(value.country_id != -1){
                    value.country_id?.let { adapter?.setSelectedId(it) }
                }
            }
        }

        binding.back.setOnClickListener {
            activity?.finish()
        }

        viewModel.getCountry()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        filterComponentViewModel.filterComponent.injectSelectCountry(this)
    }

    override fun onText(text: String) {
        var searchFilter = listCountry.filter { it.title.lowercase().contains(text.lowercase()) }
        countryAdapter.updateList(searchFilter.toMutableList())
    }

    override fun onClear() {
        countryAdapter.updateList(listCountry)
    }

    override fun select(country: Country) {
        FilterUserInfo.country.value = country
        activity?.finish()
    }

}