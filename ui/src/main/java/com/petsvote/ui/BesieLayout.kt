package com.petsvote.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.petsvote.ui.parallax.DotIndicator

open class BesieLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mOnClickListener: OnClickListener? = null
    var dotColor = ContextCompat.getColor(context, android.R.color.transparent)
        set(value) {
            field = value
            dotColor()
        }

    init {
        var inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.besie_layout, this, true)

        context.withStyledAttributes(attrs, R.styleable.BesieLayout){
            findViewById<DotIndicator>(R.id.dot).apply {
                isRipple = getBoolean(R.styleable.BesieLayout_bl_ripple, false)
                setOnClickListener{
                    mOnClickListener?.onClick(this)
                }
                pRipple.color = getColor(R.styleable.BesieLayout_bl_ripple_color,
                    ContextCompat.getColor(context, R.color.ripple_gray))
            }
            dotColor = getColor(R.styleable.BesieLayout_bl_background,
                ContextCompat.getColor(context, android.R.color.transparent))
        }

    }

    fun setLp(w: Int, h: Int){
        findViewById<DotIndicator>(R.id.dot).apply {
            widthView = w
            heightView = h
        }
    }

    fun animRipple(){
        findViewById<DotIndicator>(R.id.dot).apply {
            isAmim = true
            animRipple()
        }
    }

    private fun dotColor(){
        findViewById<DotIndicator>(R.id.dot).setDotColor(dotColor)
    }

    fun setActive(value: Boolean){
        findViewById<DotIndicator>(R.id.dot).apply {
            isRipple = value
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }
}