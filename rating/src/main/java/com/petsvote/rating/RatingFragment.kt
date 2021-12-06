package com.petsvote.rating

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.petsvote.api.entity.Pet
import com.petsvote.rating.adapter.HeaderRatingAdapter
import com.petsvote.rating.adapter.RatingPetAdapter
import com.petsvote.rating.databinding.FragmentRatingBinding
import com.petsvote.ui.BesieTransformation

class RatingFragment : Fragment(R.layout.fragment_rating) {

    private val TAG = RatingFragment::class.java.name

    private var listPet = mutableListOf<Pet>()
    private var ratingPetAdapter = RatingPetAdapter(listPet)

    private var topLinearHeight = 0
    private var topLinearContainerHeight = 0
    private var filterContainerHeight = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentRatingBinding.bind(view)
        binding.listRating.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ratingPetAdapter
        }

        binding.refresh.setColorSchemeResources(R.color.ui_primary)
        binding.refresh.setOnRefreshListener { binding.refresh.isRefreshing = false }

        var listPetIndex = Randomizer.generatePet()
        if(listPetIndex.isNotEmpty()){
           binding.topPet.root.visibility = View.VISIBLE
           binding.topPet.image.setImageDrawable(
               context?.let { ContextCompat.getDrawable(it, Randomizer.getRandomPhoto()) }
           )
            binding.topPet.location.text = "${listPetIndex[0].country_name}, " +
                    "${listPetIndex[0].city_name}"
            binding.topPet.name.text = listPetIndex[0].name
        }
        if(listPetIndex.size > 1)
            listPet.addAll(listPetIndex.subList(1, listPetIndex.size - 1))

        binding.topLinear.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(topLinearHeight == 0)
                    topLinearHeight = binding.topLinear.measuredHeight
            }
        })

        binding.topLinearContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(topLinearContainerHeight == 0)
                    topLinearContainerHeight = binding.topLinearContainer.measuredHeight
            }
        })
        binding.filterContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(filterContainerHeight == 0)
                    filterContainerHeight = binding.filterContainer.measuredHeight
            }
        })


        binding.nestedScroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.d(TAG, "linTopHeight = $topLinearHeight \n scrollY = $scrollY")
            var lpTopLinear = binding.topLinear.layoutParams
            var s = topLinearHeight - scrollY / 2
            if (s < 0) return@setOnScrollChangeListener
            lpTopLinear.height = s
            binding.topLinear.layoutParams = lpTopLinear
//            binding.topLinear.translationY = (-scrollY /2).toFloat()
//            if(scrollY in 1..filterContainerHeight)
//                binding.filterContainer.translationY = (-scrollY).toFloat()
//            var lp = binding.topLinearContainer.layoutParams
//            var sContainer = lp + scrollY / 2
//            lp.height = sContainer
//            binding.nestedScroll.layoutParams = lp
        }

        context?.let { Glide.with(it).load(R.drawable.cat6).transform(BesieTransformation(requireContext())).into(binding.img1) }
    }

}