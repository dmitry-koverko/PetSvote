package com.petsvote.base

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.petsvote.api.checkConnect

open class BaseFragment(private val idRes: Int): Fragment(idRes) {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        if(context?.let { checkConnect(it) } == false){
            Toast.makeText(context, "Error internet connection!!", Toast.LENGTH_SHORT).show()
        }
    }

}