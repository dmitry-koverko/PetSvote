package com.petsvote.app

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.petsvote.app.databinding.BlankFragmentBinding
import com.petsvote.legal.LegalFragment
import com.petsvote.register.RegisterFragment
import com.petsvote.splash.SplashFragment

class BlankFragment : Fragment(R.layout.blank_fragment) {

    private val TAG = BlankFragment::class.java.name

    private lateinit var viewModel: BlankViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = BlankFragmentBinding.bind(view)

        val adapter = activity?.supportFragmentManager?.let { context?.let { it1 -> MyAdapter(it1, it, 3) } }
        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.tabLayout.setScrollPosition(1, 0f, true)
        binding.viewPager.currentItem = 1


        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val vg = binding.tabLayout.getChildAt(0) as ViewGroup
                val tabsCount = vg.childCount

                val vgTab = vg.getChildAt(0) as ViewGroup
                val tabChildsCount = vgTab.childCount

                val tabViewChild = vgTab.getChildAt(1)
                if (tabViewChild is ImageView) {
                    var params = tabViewChild.layoutParams
                    params.width = positionOffsetPixels.toInt()
                    tabViewChild.layoutParams= params
                }
                Log.d(TAG, "onPageScrolled position = " +
                        "$position | offset = $positionOffset | positionOffsetPixels = $positionOffsetPixels")
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d(TAG, "state = $state")
            }

        })

//        var tab1: TabLayout.Tab = binding.tabLayout.newTab()
//        tab1.setCustomView(R.layout.tab)
//
//        var tab2: TabLayout.Tab = binding.tabLayout.newTab()
//        tab2.setCustomView(R.layout.tab)
//
//        var tab3: TabLayout.Tab = binding.tabLayout.newTab()
//        tab3.setCustomView(R.layout.tab)
//
//        binding.tabLayout.addTab(tab1, true)
//        binding.tabLayout.addTab(tab2)

        viewModel = ViewModelProvider(this).get(BlankViewModel::class.java)
        viewModel.get()
    }
}

class MyAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return RegisterFragment()
            }
            1 -> {
                return LegalFragment()
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return SplashFragment()
            }
            else -> return RegisterFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}