package com.petsvote.data

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow

object CropperShared {

    var cropBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)

}