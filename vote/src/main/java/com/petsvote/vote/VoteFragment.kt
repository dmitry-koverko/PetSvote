package com.petsvote.vote

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.petsvote.api.entity.Pet
import com.petsvote.ui.Star
import com.petsvote.vote.adapters.CardViewPagerAdapter
import com.petsvote.vote.databinding.FragmentVoteBinding
import com.petsvote.vote.di.VoteComponentViewModel
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class VoteFragment : Fragment(R.layout.fragment_vote), View.OnClickListener {

    @Inject
    internal lateinit var voteViewModelFactory: Lazy<VoteViewModel.Factory>

    private val voteComponentViewModel: VoteComponentViewModel by viewModels()
    private val voteViewModel: VoteViewModel by viewModels {
        voteViewModelFactory.get()
    }

    private var stars: List<View>? = null
    private var adapter: CardViewPagerAdapter? = null
    private var viewPager: ViewPager2? = null
    private lateinit var binding: FragmentVoteBinding

    var lisPets = mutableListOf<Pet>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentVoteBinding.bind(view)

        binding.star1.setOnClickListener(this)
        binding.star2.setOnClickListener(this)
        binding.star3.setOnClickListener(this)
        binding.star4.setOnClickListener(this)
        binding.star5.setOnClickListener(this)

        stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)


        lifecycleScope.launchWhenStarted { 
            voteViewModel.uiState.collect { list ->
                if(!list.isNullOrEmpty()){
                    binding.voteBar.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    //adapter!!.submitList(list as MutableList<Pet>)
                    lisPets = list as MutableList<Pet>
                    nextPet()
                }

            }
        }

        voteViewModel.getRating()
        //viewPager?.isUserInputEnabled = false
    }

    fun nextPet(){
        val displayHeight = resources.displayMetrics.heightPixels
        var cardHeight = displayHeight - (190 * resources.displayMetrics.density)

        if(!lisPets.isNullOrEmpty()){
            adapter = CardViewPagerAdapter(
                requireContext(),lisPets[0] ,
                cardHeight.toInt())
            viewPager = binding.viewPager
            viewPager?.adapter = adapter
        }
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

    private fun UIStatebonus(){
        binding.containerListEmpty.visibility = View.GONE
        binding.continButton.visibility = View.VISIBLE
        binding.card.visibility = View.VISIBLE
        binding.continButton.text = getString(R.string.contin)
    }

    private fun UIStateEmptyList(){
        binding.containerListEmpty.visibility = View.VISIBLE
        binding.continButton.visibility = View.VISIBLE
        binding.card.visibility = View.GONE
        binding.continButton.text = getString(R.string.share)
    }

    private fun startAnimaVote(rate: Int){
        for(starIndex in 0..rate){
            (stars?.get(starIndex) as Star).animRipple()
        }
        adapter?.startAnim()
        val timer = object: CountDownTimer(200, 200) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if(lisPets.isNotEmpty()) lisPets.removeAt(0)
                viewPager?.currentItem?.plus(1)?.let { viewPager?.setCurrentItem(it, true) }
                nextPet()
            }
        }

        timer.start()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        voteComponentViewModel.voteComponent.inject(this)
    }
}