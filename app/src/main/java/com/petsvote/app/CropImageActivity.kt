package com.petsvote.app

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.iqeon.profile.CropImageFragment
import me.vponomarenko.injectionmanager.x.XInjectionManager

class CropImageActivity : AppCompatActivity() {

    private val navigator: Navigator by lazy {
        XInjectionManager.findComponent<Navigator>()
    }

    val REQUEST_CROP = 104

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)

        var fr: Fragment? = null
        var it = intent
        var uri: Uri? = it.getParcelableExtra<Uri>("uri")
        var path: String? = it.getStringExtra("path")
        uri?.let {
            fr = CropImageFragment( it)
        }
        path?.let {
            fr = CropImageFragment( it)
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