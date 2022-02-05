package com.petsvote.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iqeon.profile.CropImageFragment

class CropImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CropImageFragment())
                .commitNow();
        }
    }
}