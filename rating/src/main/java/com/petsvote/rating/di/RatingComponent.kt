package com.petsvote.rating.di

import android.app.Application
import android.content.Context
import com.petsvote.api.NetworkRepository
import com.petsvote.rating.RatingFragment
import com.petsvote.room.RoomRepository
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
internal annotation class RatingScope

@[RatingScope Component(
    dependencies = [RatingDeps::class],
    modules = [RatingModule::class])]

interface RatingComponent {

    fun inject(ratingFragment: RatingFragment)

    @Component.Builder
    interface Builder{

        fun deps(ratingDeps: RatingDeps): Builder
        fun build(): RatingComponent

    }

}

@Module
internal class RatingModule{

}

interface RatingDepsProvider {

    var depsRating: RatingDeps

}

interface RatingDeps{
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository
}

val Context.ratingDepsProvider: RatingDepsProvider
    get() = when(this){
        is RatingDepsProvider -> this
        is Application -> error("Application must imolements AuthorizationDepsProvider")
        else -> applicationContext.ratingDepsProvider
    }