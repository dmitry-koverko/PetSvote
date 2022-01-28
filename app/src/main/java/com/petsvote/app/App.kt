package com.petsvote.app;

import android.app.Application;
import com.iqeon.profile.di.UPDeps
import com.iqeon.profile.di.UPDepsProvider

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.petsvote.app.di.AppComponent
import com.petsvote.app.di.DaggerAppComponent
import com.petsvote.filter.di.FilterDeps
import com.petsvote.filter.di.FilterDepsProvider
import com.petsvote.rating.di.RatingDeps
import com.petsvote.rating.di.RatingDepsProvider
import com.petsvote.register.di.RegisterDeps
import com.petsvote.register.di.RegisterDepsProvider;
import com.petsvote.splash.di.SplashDeps
import com.petsvote.splash.di.SplashDepsProvider
import com.petsvote.vote.di.VoteDeps
import com.petsvote.vote.di.VoteDepsProvider
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager

class App: Application(), RegisterDepsProvider, SplashDepsProvider, RatingDepsProvider,
    FilterDepsProvider, VoteDepsProvider, UPDepsProvider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build();
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
        XInjectionManager.bindComponentToCustomLifecycle(object : IHasComponent<Navigator> {
            override fun getComponent(): Navigator = Navigator()
        })
    }

    override var depsRegister: RegisterDeps = appComponent
    override var depsSplash: SplashDeps = appComponent
    override var depsRating: RatingDeps = appComponent
    override var depsFilter: FilterDeps = appComponent
    override var depsVote: VoteDeps = appComponent
    override var depsUP: UPDeps = appComponent
}
