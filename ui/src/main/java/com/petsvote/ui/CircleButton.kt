package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.withStyledAttributes
import android.graphics.Rect
import android.view.MotionEvent
import androidx.core.content.ContextCompat


open class CircleButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = CircleButton::class.simpleName

    private var widthView: Int = 0;
    private var heightView: Int = 0;

    private var canvas: Canvas? = null
    private var p =
        Paint().apply {
            isAntiAlias = true
            strokeWidth = 1f
            color = Color.argb(100, 255, 255, 255)
            style = Paint.Style.FILL
        }

    private var pRipple =
        Paint().apply {
            isAntiAlias = true
            color = Color.argb(10, 255, 45, 81)
            style = Paint.Style.FILL
        }

    var rippleColor = ContextCompat.getColor(context, R.color.ripple_primary)
        set(value){
            field = value
            pRipple.color = value
            invalidate()
        }
    var pColor = ContextCompat.getColor(context, R.color.white)
        set(value){
            field = value
            p.color = value
            invalidate()
        }

    private var animator: ValueAnimator? = null

    private var radius = 0f
    private var iconResources = 0

    private var xTouch = 0f;
    private var yTouth = 0f;
    private var rippleRadius = 0f;
    private var isAmim = false
    private var iconSize = 0

    private var mOnClickListener: OnClickListener? = null

    init{
        context.withStyledAttributes(attrs,R.styleable.CircleButton){
            iconResources = getResourceId(R.styleable.CircleButton_icon_src, -1)
            pColor = getColor(R.styleable.CircleButton_cb_color,
                ContextCompat.getColor(context, R.color.white))
            rippleColor = getColor(R.styleable.CircleButton_cb_ripple_color,
                ContextCompat.getColor(context, R.color.ripple_primary))
            iconSize = getInt(R.styleable.CircleButton_cb_icon_size, 0)
        }
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
            else -> Math.min(requestedHeight, desiredHeight)
        }

        widthView = width;
        heightView = height;
        radius = heightView.toFloat()/2

        setMeasuredDimension(width, height)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        try{
            this.canvas = canvas
            drawCircle()
            if(iconResources != -1) drawIcon()
            if(isAmim){
                drawRipple()
            }
        }catch (e: Exception){
            Log.d(TAG, e.toString())
        }
    }

    private fun drawRipple(){
        canvas!!.drawCircle(xTouch, yTouth, rippleRadius, pRipple!!);
        if(rippleRadius >= widthView){
            isAmim = false
            invalidate()
        }
    }

    private fun drawCircle() {
        canvas!!.drawCircle(radius, radius, radius, p!!)
        val circularPath = Path()
        circularPath.addCircle(radius, radius, radius, Path.Direction.CCW)
        canvas!!.clipPath(circularPath)
    }

    private fun drawIcon(){
        var bitmap = getVectorBitmap()
        canvas!!.drawBitmap(bitmap,
            ((widthView - bitmap.width) / 2).toFloat(),
            ((widthView - bitmap.width) / 2).toFloat(), Paint())
    }

   private fun getVectorBitmap(): Bitmap{

       var drawable = ContextCompat.getDrawable(context, iconResources)
       var size = if(iconSize == 0) drawable!!.intrinsicWidth else iconSize
       val bitmap = Bitmap.createBitmap(
           size, size, Bitmap.Config.ARGB_8888
       )
       val canvas = Canvas(bitmap)
       drawable!!.setBounds(0, 0, canvas.width, canvas.height)
       drawable!!.draw(canvas)
       return bitmap
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
        mOnClickListener?.onClick(this)
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

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }
}