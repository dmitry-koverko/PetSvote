package com.petsvote.filter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.petsvote.api.entity.City
import com.petsvote.data.FilterUserInfo
import com.petsvote.filter.R
import com.petsvote.filter.adapter.CityAdapter
import com.petsvote.filter.adapter.CountryAdapter
import com.petsvote.filter.databinding.FragmentSelectCountryBinding
import com.petsvote.filter.di.FilterComponentViewModel
import com.petsvote.filter.viewmodels.SelectCityViewModel
import com.petsvote.filter.viewmodels.SelectCountryViewModel
import com.petsvote.room.Country
import com.petsvote.room.Location
import com.petsvote.ui.SearchBar
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SelectCityFragment(private val countryId: Int): Fragment(R.layout.fragment_select_country),
    CityAdapter.OnSelectedItem, SearchBar.OnTextSearchBar {

    @Inject
    internal lateinit var sCityVMFactory: Lazy<SelectCityViewModel.Factory>

    private val filterComponentViewModel: FilterComponentViewModel by viewModels()
    private val viewModel: SelectCityViewModel by viewModels {
        sCityVMFactory.get()
    }

    private var listCity = mutableListOf<City>()
    private var adapter: CityAdapter? = null
    var cityAdapter = CityAdapter(listCity)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSelectCountryBinding.bind(view)

        this.adapter = cityAdapter
        cityAdapter.setOnSelected(this)
        binding.listCountry.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = cityAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                listCity.addAll(it)
                cityAdapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiStateLocation.collect { value: Location ->
                if(value.city_id != -1){
                    value.city_id?.let { adapter?.setSelectedId(it) }
                }
            }
        }

        viewModel.getLocation()

        var idCity = FilterUserInfo.city.value.id
        if(idCity != 0 && idCity != -1) cityAdapter?.setSelectedId(idCity)

        binding.searchBar.setOnTextSearchBar(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        filterComponentViewModel.filterComponent.injectSelectCity(this)
    }

    override fun select(city: City) {
        FilterUserInfo.city.value =
            com.petsvote.room.City(city.id, 0, city.country_id, city.title,
                "", "", 0)
        activity?.finish()
    }

    override fun onText(text: String) {
        var searchFilter = listCity.filter { it.title.lowercase().contains(text.lowercase()) ||
                it.area?.lowercase()?.contains(text.lowercase()) == true ||
                it.region?.lowercase()?.contains(text.lowercase()) == true
        }
        cityAdapter.updateList(searchFilter.toMutableList())
    }

    override fun onClear() {
        cityAdapter.updateList(listCity)
    }
}