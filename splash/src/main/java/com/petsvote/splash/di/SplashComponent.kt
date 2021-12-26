package com.petsvote.splash.di

import android.app.Application
import android.content.Context
import com.petsvote.api.NetworkRepository
import com.petsvote.splash.SplashFragment
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
internal annotation class SplashScope

@[SplashScope Component(
    dependencies = [SplashDeps::class],
    modules = [SplashModule::class])]

interface SplashComponent {

    fun inject(splashFragment: SplashFragment)

    @Component.Builder
    interface Builder{

        fun deps(splashDeps: SplashDeps): Builder
        fun build(): SplashComponent

    }

}

@Module
internal class SplashModule{

}

interface SplashDepsProvider {

    var depsSplash: SplashDeps

}

interface SplashDeps{
    val networkRepository: NetworkRepository
    //val roomRepository: RoomRepository
}

val Context.splashDepsProvider: SplashDepsProvider
    get() = when(this){
        is SplashDepsProvider -> this
        is Application -> error("Application must imolements AuthorizationDepsProvider")
        else -> applicationContext.splashDepsProvider
    }