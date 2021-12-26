package com.petsvote.register.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class RegisterComponentViewModel(application: Application): AndroidViewModel(application) {

    val registerComponent: RegisterComponent by lazy {
        DaggerRegisterComponent.builder()
            .deps(application.registerDepsProvider.depsRegister)
            .build()
    }

}