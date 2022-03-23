package com.petsvote.ui.dialogs

import android.os.Bundle
import android.view.View
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogInformationKindBinding
import com.petsvote.ui.databinding.DialogInformationSelectCountryBinding

class InformationKindDialog: BaseDialog(R.layout.dialog_information_kind) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = DialogInformationKindBinding.bind(view)
        binding.ok.setOnClickListener {
            dismiss()
        }

    }

}