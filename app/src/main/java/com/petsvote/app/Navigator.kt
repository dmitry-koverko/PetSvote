package com.petsvote.app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import com.petsvote.pet.PetActivity
import com.petsvote.ui.navigation.CropNavigation
import com.petsvote.ui.navigation.PetNavigation
import com.petsvote.ui.navigation.RegisterNavigation
import com.petsvote.ui.navigation.TabsNavigation

class Navigator: RegisterNavigation, TabsNavigation, CropNavigation, PetNavigation {

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

    override fun startSelectActivity(state: Int, currentActivity: Activity, countryId: Int) {
        var intent = Intent(currentActivity, SelectActivity::class.java)
        intent.putExtra("id", state)
        currentActivity.startActivity(intent)
    }

    override fun startCropActivity(currentActivity: Activity, bitmap: Bitmap?, path: Uri?) {
        var intent = Intent(currentActivity, CropImageActivity::class.java)
        bitmap?.let {
            intent.putExtra("bitmap", bitmap);
        }
        path?.let {
            intent.putExtra("uri", path);
        }
        currentActivity.startActivityForResult(intent,104)
    }

    override fun setResultCrop(currentActivity: Activity, bitmap: Bitmap?) {
        val intent = Intent()
        bitmap?.let {  intent.putExtra("bitmap", bitmap) }
        currentActivity.setResult((currentActivity as CropImageActivity).REQUEST_CROP, intent)
    }

    override fun toAddPet(currentActivity: Activity) {
        currentActivity.startActivity(Intent(currentActivity, PetActivity::class.java))
    }


}