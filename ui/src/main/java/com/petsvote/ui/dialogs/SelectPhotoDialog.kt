package com.petsvote.ui.dialogs

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogSelectPhotoBinding
import com.petsvote.ui.dialogs.adapter.AllPhotosAdapter
import com.petsvote.ui.entity.LocalPhoto
import java.io.FileDescriptor
import java.io.IOException

class SelectPhotoDialog: DialogFragment(R.layout.dialog_select_photo) {

    private var listPhoto = mutableListOf<LocalPhoto>()
    private var photoAdapter = AllPhotosAdapter(listPhoto)

    var CAMERA_PERMISSION = Manifest.permission.CAMERA
    var READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

    var RC_PERMISSION = 101

    private lateinit var binding: DialogSelectPhotoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogSelectPhotoBinding.bind(view)

        var mLayoutManager = LinearLayoutManager(context)
        binding.photosList.apply {
            layoutManager = mLayoutManager
            this.adapter = photoAdapter
        }
        listPhoto.add(LocalPhoto(0, ""))
        photoAdapter.submit(listPhoto)

        lifecycleScope.launchWhenStarted {
            //if (checkPermissions())  getLocalImages() else requestPermissions()
            if (checkPermissionsRead())  getLocalImages() else requestPermissionsRead()
        }

    }

    fun getLocalImages(){
        var list = mutableListOf<String>()
        context?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val id = cursor.getLong(idColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                contentUri.encodedPath?.let { list.add(it) }
                Log.d("TAG", "${contentUri.encodedPath}")
            }
        }
        var bm = uriToBitmap(Uri.parse(list[0]))
        binding.image.setImageBitmap(bm)
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

    private fun requestPermissions() {
        activity?.let { ActivityCompat.requestPermissions(it, arrayOf(CAMERA_PERMISSION), RC_PERMISSION) }
    }

    private fun requestPermissionsRead() {
        activity?.let { ActivityCompat.requestPermissions(it, arrayOf(READ_PERMISSION), RC_PERMISSION) }
    }

    private fun checkPermissions(): Boolean {
        return ((activity?.let { ActivityCompat.checkSelfPermission(it, CAMERA_PERMISSION) }) == PackageManager.PERMISSION_GRANTED
                && (ActivityCompat.checkSelfPermission(requireActivity(), CAMERA_PERMISSION)) == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkPermissionsRead(): Boolean {
        return ((activity?.let { ActivityCompat.checkSelfPermission(it, READ_PERMISSION) }) == PackageManager.PERMISSION_GRANTED
                && (ActivityCompat.checkSelfPermission(requireActivity(), READ_PERMISSION)) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            RC_PERMISSION -> {
                var allPermissionsGranted = false
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                        break
                    } else {
                        allPermissionsGranted = true
                    }
                }
                if (!allPermissionsGranted) permissionsNotGranted() else getLocalImages()
            }
        }
    }

    private fun permissionsNotGranted() {
        AlertDialog.Builder(context).setTitle("Permissions required")
            .setMessage("These permissions are required to use this app. Please allow Camera and Audio permissions first")
            .setCancelable(false)
            .setPositiveButton("Grant") { dialog, which -> requestPermissions() }
            .show()
    }
}