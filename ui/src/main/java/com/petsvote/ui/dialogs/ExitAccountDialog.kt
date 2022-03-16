package com.petsvote.ui.dialogs

import android.os.Bundle
import android.view.View
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogExitAccountBinding
import com.petsvote.ui.databinding.DialogProfileMoreBinding

class ExitAccountDialog: BaseDialog(R.layout.dialog_exit_account, true) {

    private var mExitAccountDialogListener: ExitAccountDialogListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = DialogExitAccountBinding.bind(view)
        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.exit.setOnClickListener {
            dismiss()
            mExitAccountDialogListener?.exitFromAccount()
        }

    }

    fun setExitAccountDialogListener(listener: ExitAccountDialogListener){
        mExitAccountDialogListener = listener
    }

    interface ExitAccountDialogListener{
        fun exitFromAccount()
    }
}