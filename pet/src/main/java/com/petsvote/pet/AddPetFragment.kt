package com.petsvote.pet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.petsvote.pet.adapter.ItemMoveCallback
import com.petsvote.pet.adapter.PetPhotoAdapter
import com.petsvote.pet.databinding.FragmentAddPetBinding
import com.petsvote.pet.entity.PetPhoto

class AddPetFragment: Fragment(R.layout.fragment_add_pet) {

    var touchHelper: ItemTouchHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentAddPetBinding.bind(view)

        var listPhotos = mutableListOf<PetPhoto>()
        for (i in 0..5){
            listPhotos.add(PetPhoto(null, null))
        }
        val adapter =  PetPhotoAdapter(listPhotos, requireContext())

        binding.photosPetList.layoutManager = GridLayoutManager(context, 3)
        binding.photosPetList.adapter = adapter;

        val callback: ItemTouchHelper.Callback = ItemMoveCallback(adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper!!.attachToRecyclerView(binding.photosPetList)

    }

}