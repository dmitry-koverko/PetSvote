package com.petsvote.vote.di
import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class VoteComponentViewModel(application: Application): AndroidViewModel(application) {

    val voteComponent: VoteComponent by lazy {
        DaggerVoteComponent.builder()
            .deps(application.voteDepsProvider.depsVote)
            .build()
    }

}