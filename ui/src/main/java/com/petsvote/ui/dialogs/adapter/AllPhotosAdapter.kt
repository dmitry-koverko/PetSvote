package com.petsvote.ui.dialogs.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Rational
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraXConfig
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.impl.PreviewConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.recyclerview.widget.RecyclerView
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
        this.list.addAll(list)
    }

    override fun onBindViewHolder(holder: AllPhotosHolder, position: Int) {
        val photo: LocalPhoto = list[position]
        holder.bind(photo, position)
    }

    override fun getItemCount(): Int = list.size

    inner class AllPhotosHolder(private val binding: LocalPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocalPhoto, position: Int) {
            binding.preview.setImageBitmap(item.bitmap)
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
