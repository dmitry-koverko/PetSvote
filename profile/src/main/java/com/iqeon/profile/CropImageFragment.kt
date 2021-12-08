package com.iqeon.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewTreeObserver
import com.iqeon.profile.databinding.FragmentCropImageBinding
import java.io.FileDescriptor
import java.io.IOException

class CropImageFragment : Fragment(R.layout.fragment_crop_image) {

    private var binding: FragmentCropImageBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCropImageBinding.bind(view)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat2)
        binding!!.cropView.setBitmap(bitmap)

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
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}