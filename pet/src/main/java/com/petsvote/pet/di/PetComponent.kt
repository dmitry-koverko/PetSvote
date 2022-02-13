package com.petsvote.pet.di

import android.app.Application
import android.content.Context
import com.petsvote.api.NetworkRepository
import com.petsvote.pet.addpet.AddPetFragment
import com.petsvote.pet.info.PetInfoFragment
import com.petsvote.room.RoomRepository
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
internal annotation class PetScope

@[PetScope Component(
    dependencies = [PetDeps::class],
    modules = [PetModule::class])]

interface PetComponent {

    fun inject(addPetFragment: AddPetFragment)
    fun injectInfo(petInfoFragment: PetInfoFragment)

    @Component.Builder
    interface Builder{

        fun deps(petDeps: PetDeps): Builder
        fun build(): PetComponent

    }

}

@Module
internal class PetModule{

}

interface PetDepsProvider {

    var depsPet: PetDeps

}

interface PetDeps{
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository
}

val Context.petDepsProvider: PetDepsProvider
    get() = when(this){
        is PetDepsProvider -> this
        is Application -> error("Application must imolements AuthorizationDepsProvider")
        else -> applicationContext.petDepsProvider
    }
