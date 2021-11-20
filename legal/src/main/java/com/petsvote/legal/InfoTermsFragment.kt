package com.petsvote.legal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.petsvote.legal.databinding.ActivityScrollingBinding
import android.util.Log




class InfoTermsFragment : Fragment(R.layout.activity_scrolling) {

    private val TAG = InfoTermsFragment::class.java.name

    companion object {
        @JvmStatic
        fun newInstance() =
            InfoTermsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = ActivityScrollingBinding.bind(view)

        binding.scroll.viewTreeObserver.addOnScrollChangedListener(
            object : ViewTreeObserver.OnScrollChangedListener{
                override fun onScrollChanged() {
                    val scrollY: Int = binding.scroll.scrollY
                    Log.d(TAG, "scrollY: $scrollY")


                }

            })
    }
}