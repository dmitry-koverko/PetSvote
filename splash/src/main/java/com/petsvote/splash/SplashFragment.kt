package com.petsvote.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petsvote.splash.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSplashBinding.bind(view)

    }
}