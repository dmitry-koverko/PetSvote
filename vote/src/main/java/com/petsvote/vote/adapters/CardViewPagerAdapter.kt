package com.petsvote.vote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.vote.R

class CardViewPagerAdapter(private val context: Context, private val arrayList: List<Int>) :
    RecyclerView.Adapter<CardViewPagerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.image.setImageDrawable(ContextCompat.getDrawable(context, arrayList[position]))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var image: ImageView

        init {
            //image = itemView.findViewById(R.id.image)
        }
    }

}