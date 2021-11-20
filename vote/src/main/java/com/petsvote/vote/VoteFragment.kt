package com.petsvote.vote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petsvote.vote.databinding.FragmentVoteBinding

class VoteFragment : Fragment(R.layout.fragment_vote) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentVoteBinding.bind(view)


    }

}