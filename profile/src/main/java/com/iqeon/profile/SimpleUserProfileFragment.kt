package com.iqeon.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.iqeon.profile.databinding.FragmentSimpleUserProfileBinding
import com.petsvote.ui.navigation.CropNavigation
import com.petsvote.ui.navigation.PetNavigation
import me.vponomarenko.injectionmanager.x.XInjectionManager

class SimpleUserProfileFragment: Fragment(R.layout.fragment_simple_user_profile) {

    private val navigationPet: PetNavigation by lazy {
        XInjectionManager.findComponent<PetNavigation>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSimpleUserProfileBinding.bind(view)

        binding.addPet.setOnClickListener { activity?.let { it1 -> navigationPet.toAddPet(it1) } }
    }

}