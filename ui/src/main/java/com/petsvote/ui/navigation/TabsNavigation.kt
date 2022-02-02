package com.petsvote.ui.navigation

import android.app.Activity

interface TabsNavigation {
    fun startTabsNavigation()
    fun startSelectActivity(state: Int, currentActivity: Activity,  countryId: Int)// 1 - city, 2 - country
}