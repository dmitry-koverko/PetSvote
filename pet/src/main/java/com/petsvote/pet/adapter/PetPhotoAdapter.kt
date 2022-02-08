package com.petsvote.pet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.pet.R
import com.petsvote.pet.entity.PetPhoto
import java.util.*

class PetPhotoAdapter(val modelList: List<PetPhoto>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemMoveCallback.ItemTouchHelperContract {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(modelList.get(position));
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_pet_photo, parent, false))
    }

    override fun getItemCount(): Int {
        return modelList.size;
    }

    interface ClickListener {
        fun onClick(pos: Int, aView: View)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: PetPhoto): Unit {
//            itemView.txt.text = model.name
//
//            val id = context.resources.getIdentifier(
//                model.name.toLowerCase(),
//                "drawable",
//                context.packageName
//            )
//            itemView.img.setImageResource(id)
        }
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(modelList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(modelList, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

}