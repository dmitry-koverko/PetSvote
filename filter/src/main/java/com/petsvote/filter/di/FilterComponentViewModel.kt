package com.petsvote.filter.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class FilterComponentViewModel(application: Application): AndroidViewModel(application) {

    val filterComponent: FilterComponent by lazy {
        DaggerFilterComponent.builder()
            .deps(application.filterDepsProvider.depsFilter)
            .build()
    }

}