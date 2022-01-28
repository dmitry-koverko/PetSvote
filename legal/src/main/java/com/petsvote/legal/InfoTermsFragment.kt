package com.petsvote.legal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.petsvote.legal.databinding.ActivityScrollingBinding
import android.util.Log
import com.petsvote.ui.navigation.RegisterNavigation
import me.vponomarenko.injectionmanager.x.XInjectionManager

class InfoTermsFragment : Fragment(R.layout.activity_scrolling) {

    private val TAG = InfoTermsFragment::class.java.name

    private val navigation: RegisterNavigation by lazy {
        XInjectionManager.findComponent<RegisterNavigation>()
    }

    private var headerHeight = 0
    private var scrollHeight = 0
    var lp: ViewGroup.MarginLayoutParams? = null
    var lpScroll: ViewGroup.MarginLayoutParams? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var textSp1 = 20 //* resources.displayMetrics.density
        var textSp2 = 34f //* resources.displayMetrics.density
        var textSub = 14f //* resources.displayMetrics.density

        var termsTextTitle = context?.resources?.getString(R.string.termsTitle)
        var termsTextText = context?.resources?.getString(R.string.policyText)
        var policyTextTitle = context?.resources?.getString(R.string.policyTitle)
        var policyTextText = context?.resources?.getString(R.string.policyText)

        var binding = ActivityScrollingBinding.bind(view)

        binding.scroll.viewTreeObserver.addOnScrollChangedListener(
            object : ViewTreeObserver.OnScrollChangedListener{
                override fun onScrollChanged() {
                    val scrollY: Int = binding.scroll.scrollY
                    if(headerHeight == 0) headerHeight = binding.header.height
                    if(scrollHeight == 0) scrollHeight = binding.scroll.height
                    Log.d(TAG, "header height = $headerHeight")
                    Log.d(TAG, "scrollY: $scrollY")

                    if(scrollY < headerHeight){

                        /*

                            34sp
                            30sp
                            28sp
                            24sp
                            20sp
                         */



                        var percent = scrollY * 100 / headerHeight
                        Log.d(TAG, "percent: $percent")

                        when(percent){
                            in 0..20 -> binding.expandedImage.textSize = 34f
                            in 21..40 -> binding.expandedImage.textSize = 30f
                            in 41..60 -> binding.expandedImage.textSize = 28f
                            in 80..110 -> binding.expandedImage.textSize = 20f
                        }

                        var textSizeSub = textSub * percent / 100
                        Log.d(TAG, "textSizeSub: $textSizeSub")

                        var textCahnge = textSp2 - textSizeSub
                        Log.d(TAG, "textCahnge: $textCahnge")

//                        binding.expandedImage.textSize = textCahnge
//
//                        if(lp == null) lp = binding.expandedImage.layoutParams as ViewGroup.MarginLayoutParams
//                        if(lpScroll == null) lpScroll = binding.scroll.layoutParams as ViewGroup.MarginLayoutParams
//
//                        lp?.leftMargin = 42 + percent
//                        lp?.height = headerHeight - scrollY
//                        binding.expandedImage.layoutParams = lp
//
//                        lpScroll?.height = scrollHeight + headerHeight - scrollY
//                        binding.scroll.layoutParams = lpScroll
//                        Log.d(TAG, "marginLeft = ${lp?.leftMargin}")
                    }
                }
            })
        binding.home.setOnClickListener {
            navigation.infoLegalToLegal()
        }

        var type = arguments?.getInt("type")
        when(type){
            0 -> {
                binding.title.text = termsTextTitle
                binding.expandedImage.text = termsTextTitle
                binding.text.text = termsTextText
            }
            1 -> {
                binding.title.text = policyTextTitle
                binding.expandedImage.text = policyTextTitle
                binding.text.text = policyTextText
            }
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

            }

        })
    }
}