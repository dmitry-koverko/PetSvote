package com.petsvote.ui.parallax

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.petsvote.ui.R
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.ui.loadImage


class ViewPagerAdapter(private val context: Context, private var arrayList: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder>() {

    fun update(listNew: List<String>){
        arrayList = listNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.parallax_item_image, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.loadImage(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView

        init {
            image = itemView.findViewById(R.id.image)
        }
    }

}