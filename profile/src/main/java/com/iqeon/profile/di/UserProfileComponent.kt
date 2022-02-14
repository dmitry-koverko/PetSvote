package com.iqeon.profile.di
import android.app.Application
import android.content.Context
import com.iqeon.profile.UserProfileFragment
import com.iqeon.profile.simple.SimpleUserProfileFragment
import com.petsvote.api.NetworkRepository
import com.petsvote.room.RoomRepository
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
internal annotation class UPScope

@[UPScope Component(
    dependencies = [UPDeps::class],
    modules = [UPModule::class])]

interface UserProfileComponent {

    fun inject(userProfileFragment: UserProfileFragment)
    fun injectSimple(simpleUserProfileFragment: SimpleUserProfileFragment)

    @Component.Builder
    interface Builder{

        fun deps(upDeps: UPDeps): Builder
        fun build(): UserProfileComponent

    }

}

@Module
internal class UPModule{

}

interface UPDepsProvider {

    var depsUP: UPDeps

}

interface UPDeps{
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository
}

val Context.upDepsProvider: UPDepsProvider
    get() = when(this){
        is UPDepsProvider -> this
        is Application -> error("Application must imolements AuthorizationDepsProvider")
        else -> applicationContext.upDepsProvider
    }