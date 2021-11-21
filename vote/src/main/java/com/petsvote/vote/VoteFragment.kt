package com.petsvote.vote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.petsvote.ui.Star
import com.petsvote.vote.adapters.CardViewPagerAdapter
import com.petsvote.vote.databinding.FragmentVoteBinding

class VoteFragment : Fragment(R.layout.fragment_vote), View.OnClickListener {

    private var stars: List<View>? = null
    private var adapter: CardViewPagerAdapter? = null
    private var viewPager: ViewPager2? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentVoteBinding.bind(view)

        binding.star1.setOnClickListener(this)
        binding.star2.setOnClickListener(this)
        binding.star3.setOnClickListener(this)
        binding.star4.setOnClickListener(this)
        binding.star5.setOnClickListener(this)

        stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)

        adapter = CardViewPagerAdapter(requireContext(), listOf(R.drawable.cat2, R.drawable.cat3))
        viewPager = binding.viewPager
        viewPager?.adapter = adapter
        viewPager?.isUserInputEnabled = false
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.star1 -> startAnimaVote(0)
            R.id.star2 -> startAnimaVote(1)
            R.id.star3 -> startAnimaVote(2)
            R.id.star4 -> startAnimaVote(3)
            R.id.star5 -> startAnimaVote(4)
        }
    }

    private fun startAnimaVote(rate: Int){
        for(starIndex in 0..rate){
            (stars?.get(starIndex) as Star).animRipple()
        }
        viewPager?.currentItem?.plus(1)?.let { viewPager?.setCurrentItem(it) }
    }
}