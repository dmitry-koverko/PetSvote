package com.petsvote.ui.dialogs

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.petsvote.data.UserInfo
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogInformationPhotoBinding

class InformationPhotoDialog: DialogFragment(R.layout.dialog_information_photo) {

    private var mInformationPhotoDialog: InformationPhotoDialogListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = DialogInformationPhotoBinding.bind(view)
        binding.ok.setOnClickListener {
            context?.let { it1 -> UserInfo.setApplyPhotos(it1, 1) }
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

    fun setInformationPhotoDialogListener(listener: InformationPhotoDialogListener){
        mInformationPhotoDialog = listener
    }
}

interface InformationPhotoDialogListener{
    fun apply()
}