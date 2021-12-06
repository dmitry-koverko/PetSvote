package com.petsvote.rating.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Pet
import com.petsvote.rating.Randomizer
import com.petsvote.rating.databinding.ItemRatingBinding

class RatingPetAdapter(private val list: List<Pet>) : RecyclerView.Adapter<RatingPetAdapter.RatingHolder>() {

    private var mOnClickItemListener: OnClickItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val itemBinding =
            ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        val pet: Pet = list[position]
        holder.bind(pet)
    }

    override fun getItemCount(): Int = list.size

    inner class RatingHolder(private val binding: ItemRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pet) {

            binding.image.setImageDrawable(
                ContextCompat.getDrawable(binding.root.context, Randomizer.getRandomPhoto())
            )
        }
    }

    fun setOnClickItemListener(onClickItemListener: OnClickItemListener){
        mOnClickItemListener = onClickItemListener
    }

    interface OnClickItemListener{
        fun onClick(position: Int)
    }
}
