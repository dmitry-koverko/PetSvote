package com.petsvote.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes

class BesieTab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var textTab: String= ""
    private var mOnClickListener: OnClickListener? = null

    var textColor = R.color.tab_text_color
        set(value){
            field = value
            setTextClor()
        }
    var typeTab = 0
        set(value){
            field = value
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
        context.withStyledAttributes(attrs, R.styleable.BesieTab){
            typeTab = getInt(R.styleable.BesieTab_bt_type, 0)
            textTab = getString(R.styleable.BesieTab_bt_text).toString()
        }
        inflater.inflate(R.layout.besir_tab, this, true)

        findViewById<SFTextView>(R.id.text).apply {
            text = textTab
        }

        findViewById<BesieLayout>(R.id.root).setOnClickListener{
            mOnClickListener?.onClick(this)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }

}