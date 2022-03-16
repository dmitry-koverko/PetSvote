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
import com.aemerse.cropper.CropImageView
import com.iqeon.profile.databinding.FragmentCropImageBinding
import com.petsvote.data.CropperShared
import com.petsvote.ui.navigation.CropNavigation
import com.petsvote.ui.navigation.TabsNavigation
import com.petsvote.ui.uriToBitmap
import com.petsvote.ui.uriToBitmapCamera
import me.vponomarenko.injectionmanager.x.XInjectionManager
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
        uri?.let {
            context?.uriToBitmap(it)?.let { it1 -> binding?.cropViewImage?.setImageBitmap(it1) }
        }
        path?.let {
            var bm = BitmapFactory.decodeFile(it)
            binding?.cropViewImage?.setImageBitmap(bm)
            binding?.cropViewImage?.zoomTo(2f, 0f)
        }

        //binding?.cropViewImage?.zoomTo(2f, 0f)
//        binding!!.cancel.setOnClickListener {
//            binding!!.cancelL.animRipple()
//        }
//        binding!!.cancelL.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
//            override fun onGlobalLayout() {
//                var w = binding!!.cancelL.measuredWidth
//                var h = binding!!.cancelL.measuredHeight
//                binding!!.cancelL.setLp(w, h)
//            }
//
//        })

//        binding?.cropView?.addOnCropListener(object : OnCropListener {
//            override fun onSuccess(bitmap: Bitmap) {
//                CropperShared.cropBitmap.value = bitmap
//                activity?.finish()
//            }
//
//            override fun onFailure(e: Exception) {
//                Log.d(TAG, e.message.toString())
//            }
//        })

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
    }

    fun setUri(uri: Uri){

    }


}