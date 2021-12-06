package com.petsvote.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes

class BesieTabActive @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var textColor = R.color.tab_text_color_active
        set(value){
            field = value
            setTextClor()
        }
    var textTab = ""
        set(value){
            field = value
            setText()
        }

    private fun setText() {
        findViewById<SFTextView>(R.id.text).apply {
            text = textTab
        }
    }

    private fun setTextClor() {
        var text = findViewById<SFTextView>(R.id.text)
        if(text != null){
            text.setTextColor(
                ContextCompat.getColor(context, textColor)
            )
        }
    }

    init{
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.besie_tab_active, this, true)
    }
}