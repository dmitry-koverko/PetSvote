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

        var viewPagerAdapter = ViewPagerAdapter(requireContext(),
            listOf<Int>(com.petsvote.ui.R.drawable.cat_card, com.petsvote.ui.R.drawable.cat2,
                com.petsvote.ui.R.drawable.cat3, com.petsvote.ui.R.drawable.cat4))

        var binding = FragmentPetPhotoBinding.bind(view)

        binding.viewPager.adapter = viewPagerAdapter
    }

}