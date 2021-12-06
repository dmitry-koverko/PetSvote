package com.petsvote.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class BaseLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    init {
        var im = ImageView(context)
        im.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cat2))
        var lp = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        im.layoutParams = lp
        this.addView(im)
    }

}