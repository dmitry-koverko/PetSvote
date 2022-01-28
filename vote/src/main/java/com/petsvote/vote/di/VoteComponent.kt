package com.petsvote.vote.di

import com.petsvote.vote.VoteFragment
import android.app.Application
import android.content.Context
import com.petsvote.api.NetworkRepository
import com.petsvote.room.RoomRepository
import dagger.Component
import dagger.Module
import javax.inject.Scope

@Scope
internal annotation class VoteScope

@[VoteScope Component(
    dependencies = [VoteDeps::class],
    modules = [VoteModule::class])]

interface VoteComponent {

    fun inject(voteFragment: VoteFragment)

    @Component.Builder
    interface Builder{

        fun deps(voteDeps: VoteDeps): Builder
        fun build(): VoteComponent

    }

}

@Module
internal class VoteModule{

}

interface VoteDepsProvider {

    var depsVote: VoteDeps

}

interface VoteDeps{
    val networkRepository: NetworkRepository
    val roomRepository: RoomRepository
}

val Context.voteDepsProvider: VoteDepsProvider
    get() = when(this){
        is VoteDepsProvider -> this
        is Application -> error("Application must imolements AuthorizationDepsProvider")
        else -> applicationContext.voteDepsProvider
    }