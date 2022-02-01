package com.petsvote.filter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.petsvote.api.entity.City
import com.petsvote.filter.R
import com.petsvote.filter.adapter.CityAdapter
import com.petsvote.filter.adapter.CountryAdapter
import com.petsvote.filter.databinding.FragmentSelectCountryBinding
import com.petsvote.filter.di.FilterComponentViewModel
import com.petsvote.filter.viewmodels.SelectCityViewModel
import com.petsvote.filter.viewmodels.SelectCountryViewModel
import com.petsvote.room.Country
import dagger.Lazy
import javax.inject.Inject

class SelectCityFragment: Fragment(R.layout.fragment_select_country) {

    @Inject
    internal lateinit var sCityVMFactory: Lazy<SelectCityViewModel.Factory>

    private val filterComponentViewModel: FilterComponentViewModel by viewModels()
    private val viewModel: SelectCityViewModel by viewModels {
        sCityVMFactory.get()
    }

    private var listCity = mutableListOf<City>()
    private var adapter: CityAdapter? = null
    var countryAdapter = CityAdapter(listCity)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSelectCountryBinding.bind(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        filterComponentViewModel.filterComponent.injectSelectCity(this)
    }
}