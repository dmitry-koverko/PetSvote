package com.petsvote.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Breed
import com.petsvote.filter.databinding.ItemBreedsBinding

class BreedsAdapter(private var list: MutableList<Breed>) : RecyclerView.Adapter<BreedsAdapter.BreedsHolder>() {

    private var mOnSelectedItem: OnSelectedItem? = null
    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsHolder {
        val itemBinding =
            ItemBreedsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedsHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BreedsHolder, position: Int) {
        val breed: Breed = list[position]
        holder.bind(breed, position)
    }

    fun setSelectedId(id: Int){
        selectedItem = id
        notifyDataSetChanged()
    }

    fun updateList(listNew: MutableList<Breed>){
        list = listNew
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    inner class BreedsHolder(private val binding: ItemBreedsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Breed, position: Int) {

            binding.title.text = "${item.title}"
            binding.root.setOnClickListener {
                binding.check.visibility = View.VISIBLE
                mOnSelectedItem?.select(item)
            }
            if(selectedItem == item.id) binding.check.visibility = View.VISIBLE
            else binding.check.visibility = View.GONE
        }
    }

    fun setOnSelected(listener: OnSelectedItem){
        mOnSelectedItem = listener
    }

    interface OnSelectedItem{
        fun select(breed: Breed)
    }
}

