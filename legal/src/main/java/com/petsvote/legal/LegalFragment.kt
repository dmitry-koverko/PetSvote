package com.petsvote.legal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import com.petsvote.legal.databinding.FragmentLegalBinding
import com.petsvote.ui.navigation.RegisterNavigation
import me.vponomarenko.injectionmanager.x.XInjectionManager

class LegalFragment : Fragment(R.layout.fragment_legal) {

    private val navigation: RegisterNavigation by lazy {
        XInjectionManager.findComponent<RegisterNavigation>()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentLegalBinding.bind(view)

        binding.containerTerms.setOnClickListener{
            context?.let { it1 -> ContextCompat.getColor(it1, R.color.selected_item_color) }?.let { it2 ->
                it.setBackgroundColor(
                    it2
                )
            }
        }

        binding.close.setOnClickListener {
            navigation.legalToRegister()
        }

        binding.back.setOnClickListener {
            navigation.legalToRegister()
        }

        binding.containerTerms.setOnClickListener {
            navigation.toInfoLegal(0)
        }

        binding.containerPolicy.setOnClickListener {
            navigation.toInfoLegal(1)
        }


    }

}