package com.petsvote.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.petsvote.ui.Shape

class BlankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_register, container, false)

        root.findViewById<Shape>(R.id.legal).setOnClickListener {
            Toast.makeText(context, "fdsfsdfsdfs", Toast.LENGTH_LONG).show()
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BlankFragment()
    }
}