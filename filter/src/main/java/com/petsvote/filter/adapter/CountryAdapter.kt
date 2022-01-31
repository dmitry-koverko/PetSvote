package com.petsvote.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Breed
import com.petsvote.filter.databinding.ItemBreedsBinding
import com.petsvote.filter.databinding.ItemCounrtyBinding
import com.petsvote.room.Country

class CountryAdapter(private var list: MutableList<Country>) : RecyclerView.Adapter<CountryAdapter.CountryHolder>() {

    private var mOnSelectedItem: OnSelectedItem? = null
    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val itemBinding =
            ItemCounrtyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        val country: Country = list[position]
        holder.bind(country, position)
    }

    fun setSelectedId(id: Int){
        selectedItem = id
        notifyDataSetChanged()
    }

    fun updateList(listNew: MutableList<Country>){
        list = listNew
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    inner class CountryHolder(private val binding: ItemCounrtyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Country, position: Int) {

            binding.title.text = item.title
            binding.check.visibility = if(selectedItem == item.id)  View.VISIBLE else View.GONE
        }
    }

    fun setOnSelected(listener: OnSelectedItem){
        mOnSelectedItem = listener
    }

    interface OnSelectedItem{
        fun select(breed: Country)
    }
}
