package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes

@SuppressLint("ResourceType")
class SFTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    private var mOnClickListener: OnClickListener? = null

    private var typeFaceSF = ResourcesCompat.getFont(context, R.font.myfont)
    private var rippleRadius = 0.0f
    private var animator: ValueAnimator? = null
    private var canvas: Canvas? = null

    var isAmim = false
    var animation = false
    private var xTouch = 0f;
    private var yTouth = 0f;

    init {
        includeFontPadding = false
        typeface = typeFaceSF
    }

    var pRipple =
        Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.ripple_text)
            style = Paint.Style.FILL
        }
        set(value){
            field = value
            invalidate()
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        if(isAmim) drawRipple()
    }


    private fun drawRipple(){
        if(animation){
            canvas!!.drawCircle(xTouch, yTouth, rippleRadius, pRipple!!);
            if(rippleRadius >= height/2){
                mOnClickListener?.onClick(this)
                isAmim = false
                invalidate()
            }
        }
    }

    fun animRipple(){
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_RADIUS", 0f, height/2.toFloat())

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(200)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            rippleRadius = animation.getAnimatedValue("PROPERTY_RADIUS") as Float
            invalidate()
        })
        animator!!.start()
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        xTouch = event!!.x
        yTouth = event!!.y

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isAmim = true
                animRipple()
            }
        }
        return true
    }
    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }
}