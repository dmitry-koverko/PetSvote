package com.iqeon.profile

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.iqeon.profile.databinding.FragmentSettingsProfileBinding
import com.petsvote.ui.openUrl

class SettingsProfileFragment: DialogFragment(R.layout.fragment_settings_profile) {

    private val urlIG = "https://www.instagram.com/petsvote.app/"
    private val urlFB = "https://www.facebook.com/petsvotepage/"
    private val urlTW = "https://twitter.com/petsvotea"
    private val urlTG = "https://t.me/petsvote"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, com.petsvote.ui.R.style.MyDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSettingsProfileBinding.bind(view)

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.instagram.setOnClickListener {
            openUrl(urlIG)
        }
        binding.facebook.setOnClickListener {
            openUrl(urlFB)
        }

        binding.twitter.setOnClickListener {
            openUrl(urlTW)
        }

        binding.telegram.setOnClickListener {
            openUrl(urlTG)
        }


    }

    override fun onResume() {
        super.onResume()
        if (dialog == null) return
        val window = dialog!!.window ?: return
        val width = resources.displayMetrics.widthPixels - resources.displayMetrics.density * 16
        val params: WindowManager.LayoutParams = window.attributes
        params.width = width.toInt()
        window.attributes = params
    }
}