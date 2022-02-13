package com.petsvote.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.data.Kinds
import com.petsvote.filter.databinding.ItemKindsBinding

class KindsAdapter(private val list: MutableList<Kinds>) : RecyclerView.Adapter<KindsAdapter.KindsHolder>() {

    private var mOnChangeSelect: OnChangeSelect? = null
    private var isCreate = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KindsHolder {
        val itemBinding =
            ItemKindsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KindsHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: KindsHolder, position: Int) {
        val kinds: Kinds = list[position]
        holder.bind(kinds, position)
    }

    override fun getItemCount(): Int = list.size

    inner class KindsHolder(private val binding: ItemKindsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Kinds, position: Int) {

            binding.title.text = "${item.name}"
            binding.check.setOnClickListener {
                item.select = !item.select
                binding.check.isChecked = item.select
                mOnChangeSelect?.onChange(position)
            }
            binding.check.isChecked = item.select

        }
    }

    fun setOnChange(onChangeSelect: OnChangeSelect){
        mOnChangeSelect = onChangeSelect
    }

    interface OnChangeSelect{
        fun onChange(position: Int)
    }

}
