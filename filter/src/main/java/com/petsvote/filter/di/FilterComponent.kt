package com.petsvote.filter.di

import android.app.Application
import android.content.Context
import com.petsvote.api.NetworkRepository
import com.petsvote.filter.fragments.FilterFragment
import com.petsvote.filter.fragments.SelectBreedsFragment
import com.petsvote.room.RoomRepository
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
internal annotation class FilterScope

@[FilterScope Component(
    dependencies = [FilterDeps::class],
    modules = [FilterModule::class])]

interface FilterComponent {

    fun inject(filterFragment: FilterFragment)
    fun injectSelectBreeds(selectBreedsFragment: SelectBreedsFragment)

    @Component.Builder
    interface Builder{

        fun deps(filterDeps: FilterDeps): Builder
        fun build(): FilterComponent

    }

}

@Module
internal class FilterModule{

}

interface FilterDepsProvider {

    var depsFilter: FilterDeps

}

interface FilterDeps{
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository
}

val Context.filterDepsProvider: FilterDepsProvider
    get() = when(this){
        is FilterDepsProvider -> this
        is Application -> error("Application must imolements AuthorizationDepsProvider")
        else -> applicationContext.filterDepsProvider
    }
