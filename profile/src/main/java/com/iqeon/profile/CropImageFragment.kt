package com.iqeon.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iqeon.profile.databinding.FragmentCropImageBinding

class CropImageFragment : Fragment(R.layout.fragment_crop_image) {

    private var binding: FragmentCropImageBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCropImageBinding.bind(view)

    }

}