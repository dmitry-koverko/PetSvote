package com.petsvote.pet.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.pet.databinding.ItemPetPhotoBinding
import com.petsvote.pet.entity.PetPhoto

class PetPhotoAdapter(private val list: MutableList<PetPhoto>) : RecyclerView.Adapter<PetPhotoAdapter.PetPhotoHolder>() {

    private var mOnClickItemListener: OnClickItemListener? = null
    private var mDragStartListener: OnStartDragListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetPhotoHolder {
        val itemBinding =
            ItemPetPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetPhotoHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PetPhotoHolder, position: Int) {
        val photo: PetPhoto = list[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int = list.size

    inner class PetPhotoHolder(private val binding: ItemPetPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PetPhoto) {

//            binding.root.setOnTouchListener (object : View.OnTouchListener {
//                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                    when (event?.action) {
//                        MotionEvent.ACTION_DOWN ->  mDragStartListener?.onStartDrag(binding.root);
//                    }
//
//                    return v?.onTouchEvent(event) ?: true
//                }
//            })


        }
    }

    fun setOnClickItemListener(onClickItemListener: OnClickItemListener){
        mOnClickItemListener = onClickItemListener
    }

    fun setOndragStartListener(dragStartListener: OnStartDragListener ){
        mDragStartListener = dragStartListener
    }

    interface OnClickItemListener{
        fun onClick(position: Int)
        fun onSizeCard(width: Int, height: Int)
    }
}