package com.petsvote.ui.navigation

import android.app.Activity

interface TabsNavigation {
    fun startTabsNavigation()
    fun startSelectActivity(state: Int, currentActivity: Activity)// 1 - city, 2 - country
}