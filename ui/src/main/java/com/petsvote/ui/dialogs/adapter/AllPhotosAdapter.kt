package com.petsvote.ui.dialogs.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.petsvote.ui.databinding.LocalPhotoItemBinding
import com.petsvote.ui.entity.LocalPhoto


class AllPhotosAdapter(private var list: MutableList<LocalPhoto>) : RecyclerView.Adapter<AllPhotosAdapter.AllPhotosHolder>() {

    private var mOnSelectedItem: OnSelectedItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPhotosHolder {
        val itemBinding =
            LocalPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllPhotosHolder(itemBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submit(list: List<LocalPhoto>){
        this.list = list as MutableList<LocalPhoto>
        notifyDataSetChanged()
    }

    fun addNotify(localPhoto: LocalPhoto){
        list.add(list.size, localPhoto)
        notifyItemInserted(list.size)
    }

    fun clear(){
        list.clear()
    }

    override fun onBindViewHolder(holder: AllPhotosHolder, position: Int) {
        val photo: LocalPhoto = list[position]
        holder.bind(photo, position)
    }

    override fun getItemCount(): Int = list.size

    inner class AllPhotosHolder(private val binding: LocalPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalPhoto, position: Int) {
//            item.bitmapObject?.let {
//                binding.preview.loadImage(it)
//            }
            item.bitmap?.let {
                Glide.with(binding.root.context).load(it).centerCrop().into(binding.preview)
            }
            binding.preview.setOnClickListener {
                mOnSelectedItem?.select(item)
            }
        }
    }

    fun setOnSelected(listener: OnSelectedItem){
        mOnSelectedItem = listener
    }

    interface OnSelectedItem{
        fun select(photo: LocalPhoto)
    }
}
