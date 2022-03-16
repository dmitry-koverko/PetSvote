package com.petsvote.ui.dialogs

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.petsvote.data.UserInfo
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogInformationPhotoBinding
import com.petsvote.ui.databinding.DialogInformationSelectCountryBinding

class InformationCountrySelectDialog: DialogFragment(R.layout.dialog_information_select_country) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = DialogInformationSelectCountryBinding.bind(view)
        binding.ok.setOnClickListener {
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