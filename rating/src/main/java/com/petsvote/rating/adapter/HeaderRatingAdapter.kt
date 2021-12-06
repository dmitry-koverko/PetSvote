package com.petsvote.rating.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Pet
import com.petsvote.rating.databinding.ItemRatingBinding
import com.petsvote.rating.databinding.ItemTopBinding

class HeaderRatingAdapter(private val pet: Pet?) : RecyclerView.Adapter<HeaderRatingAdapter.HeaderHolder>() {

    private var mOnClickItemListener: OnClickItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderHolder {
        val itemBinding =
            ItemTopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeaderHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HeaderHolder, position: Int) {
        val pet: Pet? = pet
        pet?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = 1

    inner class HeaderHolder(private val binding: ItemTopBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pet) {

            //binding.title.text = "Обменник ${position + 1}"
        }
    }

    fun setOnClickItemListener(onClickItemListener: OnClickItemListener){
        mOnClickItemListener = onClickItemListener
    }

    interface OnClickItemListener{
        fun onClick(position: Int)
    }
}