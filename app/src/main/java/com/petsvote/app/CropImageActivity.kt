package com.petsvote.app

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.iqeon.profile.CropImageFragment

class CropImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)

        var fr: Fragment? = null
        var it = intent
        var bitmap: Bitmap? = it.getParcelableExtra<Bitmap>("bitmap")
        var uri: Uri? = it.getParcelableExtra<Uri>("uri")
        bitmap?.let {
            fr = CropImageFragment(it, null)
        }
        uri?.let {
            fr = CropImageFragment(null, it)
        }

        if (savedInstanceState == null) {
            fr?.let { it1 ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, it1)
                    .commitNow()
            };
        }

    }
}