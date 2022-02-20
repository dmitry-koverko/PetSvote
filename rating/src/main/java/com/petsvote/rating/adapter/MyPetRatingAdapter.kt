package com.petsvote.rating.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.PetRating
import com.petsvote.rating.databinding.ItemMyPatSmallBinding
import com.petsvote.rating.databinding.ItemMyPetBinding
import com.petsvote.rating.databinding.ItemPetSearchBinding
import com.petsvote.rating.databinding.ItemRatingBinding
import com.petsvote.room.UserPets
import com.petsvote.ui.loadImage
import com.petsvote.ui.loadImageSmall

class MyPetRatingAdapter(private val list: MutableList<UserPets>) : RecyclerView.Adapter<MyPetRatingAdapter.MyPetRatingHolder>() {

    private var mOnClickItemListener: OnClickItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyPetRatingHolder {
        val itemBindingTopPet =
            ItemMyPatSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyPetRatingHolder(itemBindingTopPet)
    }

    override fun onBindViewHolder(holder: MyPetRatingHolder, position: Int) {
        val pet: UserPets = list[position]
        holder.bind(pet, position)
    }

    override fun getItemCount(): Int = list.size

    inner class MyPetRatingHolder(
        private val binding: ItemMyPatSmallBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserPets, position: Int) {
            when(position){
                0 -> binding.search.visibility = View.VISIBLE
                1 -> {
                    binding.petImageTop.visibility = View.VISIBLE
                    binding.petImageTop.loadImageSmall(item.photos[0].url)
                }
                else -> {
                    binding.petImageSmall.visibility =View.VISIBLE
                    binding.petImageSmall.loadImageSmall(item.photos[0].url)
                }
            }
        }
    }

    fun setOnClickItemListener(onClickItemListener: OnClickItemListener){
        mOnClickItemListener = onClickItemListener
    }

    interface OnClickItemListener{
        fun onClick(position: Int)
    }
}