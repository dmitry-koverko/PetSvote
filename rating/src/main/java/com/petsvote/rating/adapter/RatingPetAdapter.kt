package com.petsvote.rating.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.PetRating
import com.petsvote.rating.Randomizer
import com.petsvote.rating.databinding.ItemRatingBinding
import com.petsvote.ui.loadImage

class RatingPetAdapter(private val list: MutableList<PetRating>) : RecyclerView.Adapter<RatingPetAdapter.RatingHolder>() {

    private var mOnClickItemListener: OnClickItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val itemBinding =
            ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        val pet: PetRating = list[position]
        holder.bind(pet)
    }

    override fun getItemCount(): Int = list.size

    inner class RatingHolder(private val binding: ItemRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PetRating) {
            if(!item.photos.isNullOrEmpty()){
                binding.image.loadImage(item.photos[0].url)
            }
            binding.rate.text = item.index.toString()
            binding.name.text = item.name
            binding.location.text = "${item.country_name}, ${item.city_name}"

            binding.card.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    //Log.d("RatingPetAdapter", "width = ${binding.root.width} ## height = ${binding.root.height}")
                    mOnClickItemListener?.onSizeCard(binding.root.width, binding.root.height + binding.root.width)
                    var lp = binding.card.layoutParams
                    binding.card.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    lp.height = (binding.root.height * 0.36).toInt()
                    lp.width = binding.root.width
                    binding.card.layoutParams = lp

                }

            })

        }
    }

    fun setOnClickItemListener(onClickItemListener: OnClickItemListener){
        mOnClickItemListener = onClickItemListener
    }

    interface OnClickItemListener{
        fun onClick(position: Int)
        fun onSizeCard(width: Int, height: Int)
    }
}
