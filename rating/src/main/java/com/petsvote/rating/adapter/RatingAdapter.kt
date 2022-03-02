package com.petsvote.rating.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.PetRating
import com.petsvote.rating.R
import com.petsvote.rating.databinding.ItemRatingBinding
import com.petsvote.rating.databinding.ItemTopBinding
import com.petsvote.ui.loadImage

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class RatingAdapter(private val list: MutableList<PetRating>, private val onClickItemListener: OnClickItemListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(position){
           0 -> (holder as TopViewHolder).bind(list[position], onClickItemListener)
           else -> (holder as AllViewHolder).bind(list[position], onClickItemListener)
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TopViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> AllViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_VIEW_TYPE_HEADER
            1 -> ITEM_VIEW_TYPE_ITEM
            else -> 1
        }
    }

    class TopViewHolder private constructor(val binding: ItemTopBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(topPet: PetRating, onClickItemListener: OnClickItemListener) {
            if(!topPet.photos.isNullOrEmpty())
                binding.image.loadImage(topPet.photos[0].url)
            binding.name.text = topPet.name
            binding.location.text = "${topPet.country_name}, ${topPet.city_name}"
        }

        companion object {
            fun from(parent: ViewGroup): TopViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTopBinding.inflate(layoutInflater, parent, false)
                return TopViewHolder(binding)
            }
        }
    }


    class AllViewHolder private constructor(val binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: PetRating, onClickItemListener: OnClickItemListener) {
            if(!item.photos.isNullOrEmpty()){
                binding.image.loadImage(item.photos[0].url)
            }
            binding.rate.text = item.index.toString()
            binding.name.text = item.name
            binding.location.text = "${item.country_name}, ${item.city_name}"

            binding.image.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    onClickItemListener?.onSizeCard(binding.root.width, binding.root.height + binding.root.width)
                    var lp = binding.image.layoutParams
                    binding.image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    lp.height = binding.root.width
                    lp.width = binding.root.width
                    binding.image.layoutParams = lp

                }

            })
        }

        companion object {
            fun from(parent: ViewGroup): AllViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRatingBinding.inflate(layoutInflater, parent, false)
                return AllViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickItemListener{
        fun onClick(position: Int)
        fun onSizeCard(width: Int, height: Int)
    }

}
