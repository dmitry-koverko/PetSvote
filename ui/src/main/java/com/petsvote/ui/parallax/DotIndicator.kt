package com.petsvote.ui.parallax

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.dynamicanimation.animation.DynamicAnimation
import com.petsvote.ui.R

class DotIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = DotIndicator::class.simpleName

    private var mOnClickListener: OnClickListener? = null
    var widthView: Int = 0
        set(value){
            if(value > field){
                field = value
                invalidate()
            }
        }
    var heightView: Int = 0
        set(value) {
            if(value > field){
                field = value
                invalidate()
            }
        }
    private var canvas: Canvas? = null

    private var path = Path()

    var radius = 0f
        set(value) {
            if(value > field){
                field = value
                invalidate()
            }
        }
    private var animator: ValueAnimator? = null
    private var rippleRadius = 0f;
    var isAmim = false

    private var xTouch = 0f;
    private var yTouth = 0f;
    var pRipple =
        Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.ripple_gray)
            style = Paint.Style.FILL
        }
        set(value){
            field = value
            invalidate()
        }

    var paint =
        Paint().apply {
            isAntiAlias = true
            color = Color.argb(50, 255, 255, 255)
            style = Paint.Style.FILL
        }
        set(value){
            field = value
            invalidate()
        }
    var isRipple = false
        set(value){
            field = value
            invalidate()
        }
    var changeLayout = true

    init {
        context.withStyledAttributes(attrs, R.styleable.DotIndicator){
            paint.color =
                getColor(R.styleable.DotIndicator_dot_background,
                ContextCompat.getColor(context, android.R.color.black))
            isRipple = getBoolean(R.styleable.DotIndicator_dot_ripple, false)
        }
    }

    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        Log.d("DOTINDICATOR", "heightView =$heightView | radius = $radius")
        path.reset()
        path.moveTo(radius, 0f)
        path.quadTo(radius /16, radius /16, 0f, radius)
        path.lineTo(0f, heightView - radius)
        path.quadTo((radius /16).toFloat(), heightView -  (radius /16).toFloat(),
            radius, heightView.toFloat())
        path.lineTo(widthView - radius, heightView.toFloat());
        path.quadTo(widthView - radius/16, heightView - radius/16,
                    widthView.toFloat(), heightView - radius)
        path.lineTo(widthView.toFloat(), radius)
        path.quadTo(widthView - radius/16, radius/16,
            widthView - radius, 0f);
        path.lineTo(radius, 0f)

        canvas.clipPath(path)
        canvas.drawPath(path, paint);
        if(isAmim) drawRipple()
    }

    private fun drawRipple(){
        canvas!!.drawCircle(xTouch, yTouth, rippleRadius, pRipple!!);
        if(rippleRadius >= widthView){
            isAmim = false
            invalidate()
        }
    }

    fun animRipple(){
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_RADIUS", 0f, widthView.toFloat())

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(300)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            rippleRadius = animation.getAnimatedValue("PROPERTY_RADIUS") as Float
            invalidate()
        })
        animator!!.addListener(object : DynamicAnimation.OnAnimationEndListener,
            Animator.AnimatorListener {
            override fun onAnimationEnd(
                animation: DynamicAnimation<*>?,
                canceled: Boolean,
                value: Float,
                velocity: Float
            ) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                mOnClickListener?.onClick(this@DotIndicator)
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

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
               if(isRipple){
                   isAmim = true
                   animRipple()
               }
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val requestedWidth = MeasureSpec.getSize(widthMeasureSpec)
        val requestedWidthMode = MeasureSpec.getMode(widthMeasureSpec)

        val requestedHeight = MeasureSpec.getSize(heightMeasureSpec)
        val requestedHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        val desiredWidth: Int = 0
        val desiredHeight: Int = 0

        val width = when (requestedWidthMode) {
            MeasureSpec.EXACTLY -> requestedWidth
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> Math.min(requestedWidth, desiredWidth)
        }

        val height = when (requestedHeightMode) {
            MeasureSpec.EXACTLY -> requestedHeight
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> kotlin.math.min(requestedHeight, desiredHeight)
        }

        widthView = width;
        heightView = height;
        radius = kotlin.math.min(widthView, heightView).toFloat() /2

        xTouch = width / 2f
        yTouth = height / 2f

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if(changeLayout){
            val margins: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams::class.java.cast(layoutParams)
            val margin: Int = (7 * context.resources.displayMetrics.density).toInt()
            margins.bottomMargin = margin
            margins.rightMargin = margin
            layoutParams = margins
        }
    }

    fun setDotColor(color: Int){
        paint.color = color
        invalidate()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }
}