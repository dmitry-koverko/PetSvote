package com.petsvote.app.di

import android.app.Application
import com.iqeon.profile.di.UPDeps
import com.petsvote.api.NetworkRepository
import com.petsvote.filter.di.FilterDeps
import com.petsvote.rating.di.RatingDeps
import com.petsvote.register.di.RegisterDeps
import com.petsvote.room.RoomRepository
import com.petsvote.splash.di.SplashDeps
import com.petsvote.vote.di.VoteDeps
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@[AppScope Component(modules = [AppModule::class])]
interface AppComponent: RegisterDeps, SplashDeps, RatingDeps, FilterDeps, VoteDeps, UPDeps {

    override val networkRepository: NetworkRepository
    override val roomRepository: RoomRepository

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

}

@Module
class AppModule{

    @Provides
    @AppScope
    fun provideNetworkRepository(application: Application): NetworkRepository{
        return NetworkRepository(application)
    }
    @Provides
    @AppScope
    fun providesRoomRepository(application: Application): RoomRepository {
        return RoomRepository(application)
    }
}

@Scope
annotation class AppScope

