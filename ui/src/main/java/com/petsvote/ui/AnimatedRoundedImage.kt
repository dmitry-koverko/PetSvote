package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat

class AnimatedRoundedImage(context: Context, attrs: AttributeSet? = null) :
    RoundedImage(context, attrs) {

    private var isAnim = false

    private var mOnClickListener: OnClickListener? = null

    private var widthView: Int = 0;
    private var heightView: Int = 0;

    private var pRipple =
        Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.ripple_gray)
            style = Paint.Style.FILL
        }
    private var animator: ValueAnimator? = null
    private var xTouch = 0f;
    private var yTouth = 0f;
    private var rippleRadius = 0f;

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        widthView = right
        rippleRadius = widthView / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isAnim) drawRipple(canvas)
    }

    private fun drawRipple(canvas: Canvas){
        canvas.drawCircle(xTouch, yTouth, rippleRadius, pRipple);
        if(rippleRadius >= widthView /2){
            mOnClickListener?.onClick(this)
            isAnim = false
            invalidate()
        }
    }
    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        xTouch = event!!.x
        yTouth = event!!.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
                isAnim = true
                animRipple()
            }
        }

        return true
    }

    fun animRipple(){
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_RADIUS", 0f, widthView.toFloat())

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(400)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            rippleRadius = animation.getAnimatedValue("PROPERTY_RADIUS") as Float
            invalidate()
        })
        animator!!.start()
    }
}