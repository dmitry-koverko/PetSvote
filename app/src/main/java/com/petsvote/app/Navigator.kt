package com.petsvote.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import com.petsvote.ui.navigation.RegisterNavigation
import com.petsvote.ui.navigation.TabsNavigation

class Navigator: RegisterNavigation, TabsNavigation {

    private var navController: NavController? = null
    override fun closeRegister() {
        startTabsNavigation()
    }

    override fun startRegisterNavigation() {
        var inflater = navController?.navInflater
        var graph = inflater?.inflate(R.navigation.register_navigation)
        if (graph != null) {
            navController?.graph = graph
        }
    }

    override fun toLegal() {
        navController?.navigate(R.id.action_registerFragment_to_legalFragment)
    }

    override fun toInfoLegal(type: Int) {
        var bundle = Bundle()
        bundle.putInt("type", type)
        navController?.navigate(R.id.action_legalFragment_to_infoTermsFragment, bundle)
    }

    override fun infoLegalToLegal() {
        navController?.popBackStack()
    }

    override fun legalToRegister() {
        navController?.popBackStack()
    }

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        navController = null
    }

    override fun startTabsNavigation() {
        var inflater = navController?.navInflater
        var graph = inflater?.inflate(R.navigation.tabs_navigation)
        if (graph != null) {
            navController?.graph = graph
        }
    }

    override fun startSelectActivity(state: Int, currentActivity: Activity) {
        var intent = Intent(currentActivity, SelectActivity::class.java)
        intent.putExtra("id", state)
        currentActivity.startActivity(intent)
    }
}