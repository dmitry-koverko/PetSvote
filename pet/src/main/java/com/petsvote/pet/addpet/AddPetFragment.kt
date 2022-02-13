package com.petsvote.pet.addpet

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.data.CreatePetInfo
import com.petsvote.data.CropperShared
import com.petsvote.data.UserInfo
import com.petsvote.pet.R
import com.petsvote.pet.adapter.AddPetPhotoAdapter
import com.petsvote.pet.adapter.OnStartDragListener
import com.petsvote.pet.adapter.helper.SimpleItemTouchHelperCallback
import com.petsvote.pet.databinding.FragmentAddPetBinding
import com.petsvote.pet.di.PetComponentViewModel
import com.petsvote.pet.entity.PetPhoto
import com.petsvote.ui.dialogs.SelectPhotoDialog
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AddPetFragment: Fragment(R.layout.fragment_add_pet), OnStartDragListener,
    SelectPhotoDialog.SelectPhotoDialogListener {

    private val TAG = AddPetFragment::class.java.name

    var cal = Calendar.getInstance()
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    @Inject
    internal lateinit var addViewModelFactory: Lazy<AddPetViewModel.Factory>

    private val addCViewModel: PetComponentViewModel by viewModels()
    private val viewModel: AddPetViewModel by viewModels {
        addViewModelFactory.get()
    }


    private var mItemTouchHelper: ItemTouchHelper? = null
    var listPhotos = mutableListOf<PetPhoto>()
    private var dialogSelectPhoto = SelectPhotoDialog()

    val adapter = AddPetPhotoAdapter(activity, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myFormat = "dd.MM.yyyy"
        val serverFormat = "yyyy-MM-dd HH:mm:ss ZZZ"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val sdfServer = SimpleDateFormat(serverFormat, Locale.getDefault())

        var binding = FragmentAddPetBinding.bind(view)

        if(listPhotos.isEmpty()){
            for(i in 0..5){
                listPhotos.add(PetPhoto(null))
            }
            adapter.addData(listPhotos)
        }

        val recyclerView: RecyclerView = binding.photosPetList
        recyclerView.setHasFixedSize(true)
        recyclerView.setAdapter(adapter)

        val layoutManager = GridLayoutManager(activity, 3)
        recyclerView.setLayoutManager(layoutManager)

        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(recyclerView)

        dialogSelectPhoto.setSelectedPhotoCrop(this)

        lifecycleScope.launchWhenResumed {
            CropperShared.cropBitmap.collect { bitmap ->
                bitmap?.let {
                    if(dialogSelectPhoto.isAdded) dialogSelectPhoto.dismiss()
                    addToLiost(bitmap)
                }
            }
        }
        binding.kids.setOnClickListener {
            binding.containerKids.isPressed = true
            binding.containerKids.performClick()
        }

        binding.kidsText.setOnClickListener {
            binding.containerKids.isPressed = true
            binding.containerKids.performClick()
        }

        binding.breeds.setOnClickListener {
            binding.containerBreeds.isPressed = true
            binding.containerBreeds.performClick()
        }
        binding.breedsText.setOnClickListener {
            binding.containerBreeds.isPressed = true
            binding.containerBreeds.performClick()
        }

        binding.birthday.setOnClickListener {
            binding.containerBirthday.isPressed = true
            binding.containerBirthday.performClick()
        }
        binding.birthdayText.setOnClickListener {
            binding.containerBirthday.isPressed = true
            binding.containerBirthday.performClick()
        }

        binding.containerKids.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("create", true)
            findNavController().navigate(R.id.action_addPetFragment_to_selectKidsFragment2, bundle) }

        binding.containerBreeds.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("create", true)
            findNavController().navigate(R.id.action_addPetFragment_to_selectBreedsFragment2, bundle)
        }

        lifecycleScope.launchWhenResumed {
            CreatePetInfo.kind.collect {
                if(it.id != -1){
                    binding.kids.alpha = 1f
                    binding.kids.text = it.name
                    binding.kids.setTextColor(resources.getColor(R.color.title_color))
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            CreatePetInfo.breed.collect {
                if(it.id != -1){
                    binding.breeds.alpha = 1f
                    binding.breeds.text = it.title
                    binding.breeds.setTextColor(resources.getColor(R.color.title_color))
                }
            }
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.birthday.text = sdf.format(cal.time)
                binding.birthday.alpha = 1f
                binding.birthday.setTextColor(resources.getColor(R.color.title_color))
                Log.d(TAG, "format date for server = ${sdfServer.format(cal.time)} ")
            }
        }

        binding.containerBirthday.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        binding.save.setOnClickListener {
            var listMP = mutableListOf<MultipartBody.Part>()
            val builder: MultipartBody.Builder = MultipartBody.Builder()
            for(i in 0..listPhotos.size -1){
                if(listPhotos[i].bitmap != null){
                    listMP.add((buildImageBodyPart("photo_data[$i]",listPhotos[i].bitmap!!)))
                }
//                if(i.bitmap != null){
////                    val randomString = (1..15)
////                        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
////                        .map(charPool::get)
////                        .joinToString("");
//                   // builder.addPart(buildImageBodyPart("photo_data", i.bitmap!!))
//
//                }
            }
            //val requestBody = builder.build()
            viewModel.saveUserPet(listMP)
        }

        Log.d(TAG, "bearer = ${UserInfo.getBearer(requireContext())}")
    }

    private fun addToLiost(bitmap: Bitmap) {
        for(i in listPhotos){
            if(i.bitmap == null) {
                i.bitmap = bitmap
                adapter.notifyDataSetChanged()
                return
            }

        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper?.startDrag(viewHolder);
    }

    override fun onClick() {
        try{
            if(!dialogSelectPhoto.isAdded)
                activity?.supportFragmentManager?.let {
                        it1 -> dialogSelectPhoto.show(it1, "SelectPhotoDialog") }
        }catch (e: Exception){}
    }

    override fun crop(bitmap: Bitmap) {

    }

    private fun buildImageBodyPart(fileName: String, bitmap: Bitmap):  MultipartBody.Part {
        val leftImageFile = convertBitmapToFile(fileName, bitmap)
        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), leftImageFile)
        return MultipartBody.Part.createFormData(fileName,     leftImageFile.name, reqFile)
    }
    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(context?.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addCViewModel.petComponent.inject(this)
    }
}