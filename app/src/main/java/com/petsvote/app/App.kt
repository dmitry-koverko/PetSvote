package com.petsvote.app;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.petsvote.app.di.AppComponent
import com.petsvote.app.di.DaggerAppComponent
import com.petsvote.register.di.RegisterDeps
import com.petsvote.register.di.RegisterDepsProvider;
import com.petsvote.splash.di.SplashDeps
import com.petsvote.splash.di.SplashDepsProvider

class App: Application(), RegisterDepsProvider, SplashDepsProvider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build();
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
    }

    override var depsRegister: RegisterDeps = appComponent
    override var depsSplash: SplashDeps = appComponent
}
