package com.iqeon.profile.di
import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class UserProfileComponentViewModel(application: Application): AndroidViewModel(application) {

    val ratingComponent: UserProfileComponent by lazy {
        DaggerUserProfileComponent.builder()
            .deps(application.upDepsProvider.depsUP)
            .build()
    }

}
