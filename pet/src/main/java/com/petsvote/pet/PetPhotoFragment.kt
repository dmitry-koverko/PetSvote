package com.petsvote.pet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petsvote.pet.databinding.FragmentPetPhotoBinding
import com.petsvote.ui.parallax.ViewPagerAdapter

class PetPhotoFragment : Fragment(R.layout.fragment_pet_photo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var viewPagerAdapter = ViewPagerAdapter(requireContext(), listOf())

        var binding = FragmentPetPhotoBinding.bind(view)

        binding.viewPager.adapter = viewPagerAdapter
    }

}