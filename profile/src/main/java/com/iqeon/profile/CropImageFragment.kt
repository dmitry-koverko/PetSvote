package com.iqeon.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewTreeObserver
import com.iqeon.profile.databinding.FragmentCropImageBinding
import com.petsvote.ui.uriToBitmap
import java.io.FileDescriptor
import java.io.IOException

class CropImageFragment(private val bitmap: Bitmap?, private val uri: Uri?) : Fragment(R.layout.fragment_crop_image) {

    private var binding: FragmentCropImageBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCropImageBinding.bind(view)
        bitmap?.let{ binding?.cropView?.setBitmap(it)}
        uri?.let { context?.uriToBitmap(it)?.let { it1 -> binding?.cropView?.setBitmap(it1) } }
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

        binding?.crop?.setOnClickListener {
            binding?.cropView?.crop()
        }
    }

    fun setUri(uri: Uri){

    }

    fun setBitmap(bitmap: Bitmap){
        binding?.cropView?.setBitmap(bitmap)
    }

}