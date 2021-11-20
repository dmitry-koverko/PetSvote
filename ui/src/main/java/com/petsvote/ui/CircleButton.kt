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
    private var p: Paint? = null
    private val pRipple =
        Paint().apply {
            isAntiAlias = true
            color = Color.argb(10, 255, 45, 81)
            style = Paint.Style.FILL
        }

    private var animator: ValueAnimator? = null

    private var radius = 0f
    private var iconResources = 0

    private var xTouch = 0f;
    private var yTouth = 0f;
    private var rippleRadius = 0f;
    private var isAmim = false

    private var mOnClickListener: OnClickListener? = null

    init{
        p = Paint(Paint.ANTI_ALIAS_FLAG)
        p!!.strokeWidth = 1f
        p!!.setColor(Color.argb(100, 255, 255, 255));
        //p!!.setColor(Color.RED)
        p!!.setStyle(Paint.Style.FILL);

        context.withStyledAttributes(attrs,R.styleable.CircleButton){
            iconResources = getResourceId(R.styleable.CircleButton_icon_src, -1)
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
        //var heightBitmap = 20 * resources.displayMetrics.density
        var bitmap = getVectorBitmap()
        val source = Rect(0, 0, bitmap.height, bitmap.height)
        val bitmapRect = Rect(0, 0, bitmap.height, bitmap.height    )
        canvas!!.drawBitmap(bitmap,
            ((widthView - bitmap.width) / 2).toFloat(),
            ((widthView - bitmap.width) / 2).toFloat(), Paint())
    }

   private fun getVectorBitmap(): Bitmap{

       var drawable = ContextCompat.getDrawable(context, iconResources)
       val bitmap = Bitmap.createBitmap(
           drawable!!.intrinsicWidth,
           drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888
       )
       val canvas = Canvas(bitmap)
       drawable.setBounds(0, 0, canvas.width, canvas.height)
       drawable.draw(canvas)
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