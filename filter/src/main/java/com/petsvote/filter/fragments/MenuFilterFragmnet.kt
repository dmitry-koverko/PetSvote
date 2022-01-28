package com.petsvote.filter.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.petsvote.filter.R
import com.petsvote.filter.databinding.FragmentFilterMenuBinding

class MenuFilterFragment: Fragment(R.layout.fragment_filter_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentFilterMenuBinding.bind(view)

    }

}