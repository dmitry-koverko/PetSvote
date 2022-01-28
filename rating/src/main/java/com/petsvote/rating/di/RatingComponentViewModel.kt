package com.petsvote.rating.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel

internal class RatingComponentViewModel(application: Application): AndroidViewModel(application) {

    val ratingComponent: RatingComponent by lazy {
        DaggerRatingComponent.builder()
            .deps(application.ratingDepsProvider.depsRating)
            .build()
    }

}
