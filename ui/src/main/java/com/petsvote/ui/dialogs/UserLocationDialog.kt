package com.petsvote.ui.dialogs

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.petsvote.ui.R

class UserLocationDialog: DialogFragment(R.layout.fragment_dialog_user_location) {

    private var mUserLocationDialogListener: UserLocationDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
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

    fun setUserLocationDialogListener(listener: UserLocationDialogListener){
        mUserLocationDialogListener = listener
    }

    interface UserLocationDialogListener{
        fun next()
    }

}