package com.iqeon.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.iqeon.profile.databinding.FragmentCropImageBinding
import com.petsvote.data.CropperShared
import com.petsvote.ui.*
import com.petsvote.ui.navigation.CropNavigation
import com.petsvote.ui.navigation.TabsNavigation
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

class CropImageFragment(): Fragment(R.layout.fragment_crop_image) {

    private var uri: Uri? = null
    private var path: String? = null

    constructor(_uri: Uri?) : this() {
        uri = _uri
    }
    constructor(_uri: String?) : this() {
        path = _uri
    }

    private var TAG = CropImageFragment::class.java.name
    private var binding: FragmentCropImageBinding? = null

    private val navigation: CropNavigation by lazy {
        XInjectionManager.findComponent<CropNavigation>()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCropImageBinding.bind(view)
        binding?.cropViewImage?.setCropperType(1)
        //binding?.cropViewImage?.setImageResource(R.drawable.cat2)
        uri?.let {
            binding?.cropViewImage?.let { it1 -> uriToBitmapGlide(it, it1) }
        }
        path?.let {
            val contentResolver =
                requireContext().contentResolver
            var bt= uriToBitmap(Uri.fromFile( File(it)), 1f, contentResolver)
            //var bm = BitmapFactory.decodeFile(it)
            binding?.cropViewImage?.setImageBitmap(bt)
        }


        binding?.crop?.setOnClickListener {
            val cropped: Bitmap? = binding?.cropViewImage?.getCroppedImage()
            cropped?.let { bitmap ->
                CropperShared.cropBitmap.value = bitmap
                activity?.finish()
            }

        }
        binding?.cancel?.setOnClickListener {
            CropperShared.cropBitmap.value = null
            activity?.finish()
        }

        binding?.crop?.animation = true
        binding?.cancel?.animation = true
    }


}