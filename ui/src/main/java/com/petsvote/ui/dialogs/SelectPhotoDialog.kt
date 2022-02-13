package com.petsvote.ui.dialogs

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.petsvote.data.CropperShared
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogSelectPhotoBinding
import com.petsvote.ui.dialogs.adapter.AllPhotosAdapter
import com.petsvote.ui.entity.LocalPhoto
import com.petsvote.ui.navigation.CropNavigation
import com.petsvote.ui.uriToBitmap
import kotlinx.coroutines.flow.collect
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.io.FileDescriptor
import java.io.IOException


class SelectPhotoDialog: DialogFragment(R.layout.dialog_select_photo),
    AllPhotosAdapter.OnSelectedItem {

    private val TAG = SelectPhotoDialog::class.java.name

    private lateinit var  cameraProvider: ProcessCameraProvider

    private var listPhoto = mutableListOf<LocalPhoto>()
    private var photoAdapter = AllPhotosAdapter(listPhoto)

    var CAMERA_PERMISSION = Manifest.permission.CAMERA
    var READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

    private var RC_PERMISSION = 101
    private val REQUEST_ID = 123
    private val PICK_PHOTO_CODE = 1046
    private val CROP_REQUEST = 104

    private lateinit var binding: DialogSelectPhotoBinding

    private var imageCapture: ImageCapture? = null

    private val navigationCrop: CropNavigation by lazy {
        XInjectionManager.findComponent<CropNavigation>()
    }

    private var mSelectPhotoDialogListener: SelectPhotoDialogListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogSelectPhotoBinding.bind(view)

        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.photosList.apply {
            layoutManager = mLayoutManager
            this.adapter = photoAdapter
        }

        lifecycleScope.launchWhenStarted {
            if(checkPermissions()) startCamera() else requestPermissions()
            if (checkPermissionsRead())  getLocalImages() else requestPermissionsRead()
        }


        binding.cancel.setOnClickListener { dismiss() }
        binding.allPhotos.setOnClickListener { pickPhoto() }
        binding.viewFinder.setOnClickListener { launchCameraRawPhoto() }

        photoAdapter?.setOnSelected(this)

    }

    fun setSelectedPhotoCrop(listener: SelectPhotoDialogListener){
        mSelectPhotoDialogListener = listener
    }

    private fun pickPhoto(){
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        if (context?.getPackageManager()?.let { intent.resolveActivity(it) } != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }

    }

    fun launchCameraRawPhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === Activity.RESULT_OK && data != null && requestCode == REQUEST_ID) {
            var bitmap = data.extras?.get("data") as Bitmap
            bitmap?.let { startCrop(it, null) }
        }else if (resultCode === Activity.RESULT_OK && data != null && requestCode == PICK_PHOTO_CODE) {
            val photoUri: Uri? = data.data
            photoUri?.let { startCrop(null, it) }

        }else if (resultCode === Activity.RESULT_OK && data != null && requestCode == CROP_REQUEST) {
            var bitmap: Bitmap? = data.getParcelableExtra<Bitmap>("bitmap")
            bitmap?.let {
                mSelectPhotoDialogListener?.crop(it)
                dismiss()
            }
        }
    }

    private fun startCrop(bitmap: Bitmap?, uri: Uri?){
        bitmap?.let {
            activity?.let { navigationCrop.startCropActivity(requireActivity(), bitmap, null) }
        }
        uri?.let {
            activity?.let { navigationCrop.startCropActivity(requireActivity(), null, uri) }
        }
    }
    private fun startCamera() {
        val cameraProviderFuture = context?.let { ProcessCameraProvider.getInstance(it) }

        cameraProviderFuture?.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e("TAG", "Use case binding failed", exc)
            }

        }, context?.let { ContextCompat.getMainExecutor(it) })
    }

    fun getLocalImages(){
        var list = mutableListOf<LocalPhoto>()
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
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                list.add(LocalPhoto(context?.uriToBitmap(contentUri)))
            }
        }
        photoAdapter?.submit(list)
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

    override fun onResume() {
        super.onResume()
        if (dialog == null) return
        val window = dialog!!.window ?: return
        val width = resources.displayMetrics.widthPixels - resources.displayMetrics.density * 16
        val height = resources.displayMetrics.density * 308
        val params: WindowManager.LayoutParams = window.attributes
        params.width = width.toInt()
        params.height = height.toInt()
        params.gravity = Gravity.BOTTOM
        window.attributes = params
    }

    interface SelectPhotoDialogListener{
        fun crop(bitmap: Bitmap)
    }

    override fun select(photo: LocalPhoto) {
        startCrop(photo.bitmap, null)
    }
}