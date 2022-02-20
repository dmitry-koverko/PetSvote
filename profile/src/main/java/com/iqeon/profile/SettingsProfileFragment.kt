package com.iqeon.profile

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.iqeon.profile.databinding.FragmentSettingsProfileBinding

class SettingsProfileFragment: DialogFragment(R.layout.fragment_settings_profile) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSettingsProfileBinding.bind(view)

        binding.close.setOnClickListener {
            dismiss()
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