package com.petsvote.ui.dialogs

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.petsvote.ui.R

open class BaseDialog(private val resId: Int, private val isMath: Boolean = false): DialogFragment(resId) {

    //abstract val TAG: String

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
        if(isMath){
            val height =
                resources.displayMetrics.heightPixels - resources.displayMetrics.density * 16
            params.height = height.toInt()
        }
        window.attributes = params
    }
}