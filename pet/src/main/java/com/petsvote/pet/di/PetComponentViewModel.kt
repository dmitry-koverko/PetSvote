package com.petsvote.pet.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel


internal class PetComponentViewModel(application: Application): AndroidViewModel(application) {

    val petComponent: PetComponent by lazy {
        DaggerPetComponent.builder()
            .deps(application.petDepsProvider.depsPet)
            .build()
    }

}
