package com.petsvote.splash.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class SplashComponentViewModel(application: Application): AndroidViewModel(application) {

    val splashComponent: SplashComponent by lazy {
        DaggerSplashComponent.builder()
            .deps(application.splashDepsProvider.depsSplash)
            .build()
    }

}