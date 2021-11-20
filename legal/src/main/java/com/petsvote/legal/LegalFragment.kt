package com.petsvote.legal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.petsvote.legal.databinding.FragmentLegalBinding

class LegalFragment : Fragment(R.layout.fragment_legal) {

    companion object {
        @JvmStatic
        fun newInstance() = LegalFragment()
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
    }

}