package com.petsvote.filter.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.petsvote.data.CreatePetInfo
import com.petsvote.data.FilterBreed
import com.petsvote.data.FilterPetsObject
import com.petsvote.data.Kinds
import com.petsvote.filter.R
import com.petsvote.filter.adapter.KindsAdapter
import com.petsvote.filter.databinding.FragmentSelectKindsBinding
import kotlinx.coroutines.flow.collect
import java.io.Serializable

class SelectKidsFragment: Fragment(R.layout.fragment_select_kinds), KindsAdapter.OnChangeSelect {

    private var isAll = true
    private var isCreatePet = false

    private var listKinds = mutableListOf<Kinds>(
        Kinds(0, "Кошки", true, "cat", 30),
        Kinds(1, "Собаки", true, "dog", 30),
        Kinds(2, "Лошади", true, "horse", 45),
        Kinds(3, "Хорьки", true, "ferret", 15),
        Kinds(4, "Рептилии", true, "reptile", 200),
        Kinds(5, "Грызуны", true, "rodent", 20),
        Kinds(6, "Амфибии", true, "amphibian", 30),
        Kinds(7, "Птицы", true, "bird", 90),
        Kinds(8, "Экзотика", true, "exotic", 60),
        Kinds(9, "Беспозвоночные", true, "invertebrates", 100),
        Kinds(10, "Аквариумные рыбки", true, "fish", 35),
    )

    private lateinit var binding: FragmentSelectKindsBinding

    private var adapter: KindsAdapter? = null
    var kindsAdapter = KindsAdapter(listKinds)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectKindsBinding.bind(view)

        this.adapter = kindsAdapter
        kindsAdapter.setOnChange(this)
        binding.list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = kindsAdapter
        }

        var bundle = arguments
        bundle?.let {
            isCreatePet = it.getBoolean("create", false)
            binding.containerAll.visibility = View.GONE
            if(isCreatePet){
                for(i in listKinds)
                    i.select = false
                lifecycleScope.launchWhenStarted {
                    CreatePetInfo.kind.collect { kind ->
                        if(kind.id != -1){
                            listKinds.find { it.id == kind.id}?.select = true
                            kindsAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        binding.back.setOnClickListener {
            if(!isCreatePet){
                var listStringType = mutableListOf<Kinds>()
                for(i in listKinds){
                    if(i.select) listStringType.add(i)
                }
                if(listStringType.isNotEmpty() && listStringType.size != listKinds.size)
                    FilterPetsObject._listKinds.value = listStringType
                else FilterPetsObject._listKinds.value = listOf()
                FilterPetsObject._breed.value = FilterBreed(0, "")
                findNavController().popBackStack()
            }else {
                if(listKinds.isNotEmpty()){
                    var listSelect = listKinds.filter { it.select }
                    if(listSelect.isNotEmpty()) CreatePetInfo.kind.value = listSelect.first()
                }
                findNavController().popBackStack()
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        binding.rbtn.setOnClickListener {
            isAll = !isAll
            binding.rbtn.isChecked = isAll
            for (i in listKinds){
                i.select = isAll
            }
            kindsAdapter.notifyDataSetChanged()
        }

        lifecycleScope.launchWhenStarted {
            if(!isCreatePet) getListKinds()
        }

    }

    private suspend fun getListKinds() {
        FilterPetsObject.listKinds.collect { list ->
            if (!list.isNullOrEmpty()){
                //var list: MutableList<Kinds> = listKinds as MutableList<Kinds>
                for(ii in listKinds){
                    var e = list.find { it.id == ii.id}
                    ii.select = e != null
                }
                binding.rbtn.isChecked = false
                isAll = false
                kindsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onChange(position: Int) {
       if(!isCreatePet){
           var countTrue = 0
           var countFalse = 0

           for (i in listKinds){
               if(i.select) countTrue++
               else countFalse ++
           }
           binding.rbtn.isChecked =
               when (listKinds.size) {
                   countTrue -> true
                   countFalse -> false
                   else -> false
               }
           isAll = binding.rbtn.isChecked
       }else {
            for(i in listKinds){
                i.select = false
            }
            listKinds[position].select = true
            kindsAdapter.notifyDataSetChanged()
       }
    }

}
