package com.iqeon.profile.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iqeon.profile.databinding.ItemUserPetBinding
import com.petsvote.api.entity.Breed
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.UserPets
import com.petsvote.ui.loadImage

class MyPetAdapter(private var list: MutableList<UserPets>) : RecyclerView.Adapter<MyPetAdapter.MyPetHolder>() {

    private var mOnSelectedItem: OnSelectedItem? = null
    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetHolder {
        val itemBinding =
            ItemUserPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPetHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyPetHolder, position: Int) {
        val pet: UserPets = list[position]
        holder.bind(pet, position)
    }

    fun setSelectedId(id: Int){
        selectedItem = id
        notifyDataSetChanged()
    }

    fun updateList(listNew: MutableList<UserPets>){
        list = listNew
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    inner class MyPetHolder(private val binding: ItemUserPetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserPets, position: Int) {
            binding.namePet.text = item.name
            if(item.photos?.isNotEmpty() == true){
                item.photos?.get(0)?.url?.let { binding.petImg.loadImage(it) }
            }
        }
    }

    fun setOnSelected(listener: OnSelectedItem){
        mOnSelectedItem = listener
    }

    interface OnSelectedItem{
        fun select(breed: Breed)
    }
}