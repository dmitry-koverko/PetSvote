package com.petsvote.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes

@SuppressLint("ResourceType")
class SFTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    var typeFaceSF = ResourcesCompat.getFont(context, R.font.myfont)

    init {
        includeFontPadding = false
        typeface = typeFaceSF
    }

}