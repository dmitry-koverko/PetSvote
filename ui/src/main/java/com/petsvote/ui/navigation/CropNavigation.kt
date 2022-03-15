package com.petsvote.ui.navigation

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri

interface CropNavigation {
    fun startCropActivity(currentActivity: Activity, path: Uri?)
    fun startCropActivity(currentActivity: Activity, path: String?)
    fun setResultCrop(currentActivity: Activity,bitmap: Bitmap?)
}