package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

@SuppressLint("ResourceType")
class SimpleSFTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    private var typeFaceSF = ResourcesCompat.getFont(context, R.font.myfont)
    init {
        includeFontPadding = false
        typeface = typeFaceSF
    }

}