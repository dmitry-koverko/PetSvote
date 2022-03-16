package com.petsvote.ui.dialogs

import android.os.Bundle
import android.view.View
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogExitAccountBinding
import com.petsvote.ui.databinding.DialogRemoveAccountBinding

class RemoveAccountDialog: BaseDialog(R.layout.dialog_remove_account, true) {

    private var mRemoveAccountDialogListener: RemoveAccountDialogListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = DialogRemoveAccountBinding.bind(view)
        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.remove.setOnClickListener {
            dismiss()
            mRemoveAccountDialogListener?.removeAccount()
        }

    }

    fun setRemoveAccountDialogListener(listener: RemoveAccountDialogListener){
        mRemoveAccountDialogListener = listener
    }

    interface RemoveAccountDialogListener{
        fun removeAccount()
    }
}