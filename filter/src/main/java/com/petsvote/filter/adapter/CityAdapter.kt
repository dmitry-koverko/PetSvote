package com.petsvote.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Breed
import com.petsvote.api.entity.City
import com.petsvote.filter.databinding.ItemBreedsBinding
import com.petsvote.filter.databinding.ItemCityBinding
import com.petsvote.filter.databinding.ItemCounrtyBinding
import com.petsvote.room.Country

class CityAdapter(private var list: MutableList<City>) : RecyclerView.Adapter<CityAdapter.CityHolder>() {

    private var mOnSelectedItem: OnSelectedItem? = null
    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val itemBinding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val country: City = list[position]
        holder.bind(country, position)
    }

    fun setSelectedId(id: Int){
        selectedItem = id
        notifyDataSetChanged()
    }

    fun updateList(listNew: MutableList<City>){
        list = listNew
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    inner class CityHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: City, position: Int) {

            binding.titile.text = item.title
            binding.check.visibility = if(selectedItem == item.id)  View.VISIBLE else View.GONE
            if(item.region?.isNotEmpty() == true){
                binding.subText.visibility = View.VISIBLE
                binding.subText.text = item.region
            }

            binding.root.setOnClickListener {
                binding.root.isPressed = true
                mOnSelectedItem?.select(item)
            }
        }
    }

    fun setOnSelected(listener: OnSelectedItem){
        mOnSelectedItem = listener
    }

    interface OnSelectedItem{
        fun select(city: City)
    }
}
