package com.petsvote.register.di

import android.app.Application
import android.content.Context
import com.petsvote.api.NetworkRepository
import com.petsvote.register.RegisterFragment
import com.petsvote.room.RoomRepository
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
internal annotation class RegisterScope

@[RegisterScope Component(
    dependencies = [RegisterDeps::class],
    modules = [RegisterModule::class])]
interface RegisterComponent {

    fun inject(registerFragment: RegisterFragment)

    @Component.Builder
    interface Builder{

        fun deps(registerDeps: RegisterDeps): Builder
        fun build(): RegisterComponent

    }

}

@Module
internal class RegisterModule{

}

interface RegisterDepsProvider {

    var depsRegister: RegisterDeps

}

interface RegisterDeps{
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository
}

val Context.registerDepsProvider: RegisterDepsProvider
    get() = when(this){
        is RegisterDepsProvider -> this
        is Application -> error("Application must imolements AuthorizationDepsProvider")
        else -> applicationContext.registerDepsProvider
    }