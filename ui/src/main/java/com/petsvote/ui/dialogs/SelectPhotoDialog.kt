package com.petsvote.ui.dialogs

import android.Manifest
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogSelectPhotoBinding
import com.petsvote.ui.dialogs.adapter.AllPhotosAdapter
import com.petsvote.ui.entity.LocalPhoto
import com.petsvote.ui.navigation.CropNavigation
import com.petsvote.ui.uriToBitmap
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SelectPhotoDialog: DialogFragment(R.layout.dialog_select_photo),
    AllPhotosAdapter.OnSelectedItem {

    private val TAG = SelectPhotoDialog::class.java.name

    private lateinit var  cameraProvider: ProcessCameraProvider

    private var listPhoto = mutableListOf<LocalPhoto>()
    private var photoAdapter = AllPhotosAdapter(listPhoto)

    var CAMERA_PERMISSION = Manifest.permission.CAMERA
    var READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

    private val REQUEST_ID = 123
    private val PICK_PHOTO_CODE = 1046
    private val CROP_REQUEST = 104

    private lateinit var binding: DialogSelectPhotoBinding
    private var animator: ValueAnimator? = null

    private var imageCapture: ImageCapture? = null

    private val navigationCrop: CropNavigation by lazy {
        XInjectionManager.findComponent<CropNavigation>()
    }

    lateinit var currentPhotoPath: String
    lateinit var photoURIReq: Uri

    private var mSelectPhotoDialogListener: SelectPhotoDialogListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogSelectPhotoBinding.bind(view)

        var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.photosList.apply {
            layoutManager = mLayoutManager
            this.adapter = photoAdapter
        }

        if(checkPermissions()) startCamera()
        if(checkPermissionsRead())  getLocalImages()
        if(!checkPermissionsRead() && checkPermissions()) dismiss()

        binding.cancel.setOnClickListener { dismiss() }
        binding.allPhotos.setOnClickListener { pickPhoto() }
        binding.viewFinder.setOnClickListener { launchCameraRawPhoto() }

        photoAdapter?.setOnSelected(this)
        startAnimtoTop()
    }

    private fun startAnimtoTop() {
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("TRANSITIONY", 500f, 0f)

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(200)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            var pointY = animation.getAnimatedValue("TRANSITIONY") as Float
            binding.card.translationY = pointY
        })
        animator!!.start()
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
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            //TODO rtoast permission
            null
        }

        photoFile?.let {
            photoURIReq = FileProvider.getUriForFile(
                requireContext(),
                "com.example.android.fileprovider",
                photoFile);
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURIReq);
            startActivityForResult(cameraIntent, REQUEST_ID)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === Activity.RESULT_OK && requestCode == REQUEST_ID) {
            var bm = BitmapFactory.decodeFile(currentPhotoPath)
            startCrop(currentPhotoPath)
        }else if (resultCode === Activity.RESULT_OK && data != null && requestCode == PICK_PHOTO_CODE) {
            val photoUri: Uri? = data.data
            photoUri?.let { startCrop(photoUri) }

        }else if (resultCode === Activity.RESULT_OK && data != null && requestCode == CROP_REQUEST) {
            var bitmap: Bitmap? = data.getParcelableExtra<Bitmap>("bitmap")
            bitmap?.let {
                mSelectPhotoDialogListener?.crop(it)
                dismiss()
            }
        }
    }

    private fun startCrop(uri: Uri?){
        uri?.let {
            activity?.let { navigationCrop.startCropActivity(requireActivity(),uri) }
        }
    }

    private fun startCrop(uri: String?){
        uri?.let {
            activity?.let { navigationCrop.startCropActivity(requireActivity(), uri) }
        }
    }
    private fun startCamera() {
        binding.cardPreview.visibility = View.VISIBLE
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
                list.add(LocalPhoto(contentUri))
            }
        }
        photoAdapter?.submit(list)
    }


    private fun checkPermissions(): Boolean {
        return ((activity?.let { ActivityCompat.checkSelfPermission(it, CAMERA_PERMISSION) }) == PackageManager.PERMISSION_GRANTED
                && (ActivityCompat.checkSelfPermission(requireActivity(), CAMERA_PERMISSION)) == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkPermissionsRead(): Boolean {
        return ((activity?.let { ActivityCompat.checkSelfPermission(it, READ_PERMISSION) }) == PackageManager.PERMISSION_GRANTED
                && (ActivityCompat.checkSelfPermission(requireActivity(), READ_PERMISSION)) == PackageManager.PERMISSION_GRANTED)
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
        startCrop(photo.bitmap)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}