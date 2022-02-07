package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.get
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

open class BesieImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val TAG = BesieImage::class.simpleName

    private var widthView: Int = 0;
    private var heightView: Int = 0;

    private var canvas: Canvas? = null
    private var path = Path()

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

    var rippleColor = ContextCompat.getColor(context, android.R.color.transparent)
        set(value){
            field = value
            pRipple.color = value
            invalidate()
        }
    var pColor = ContextCompat.getColor(context, R.color.ui_gray)
        set(value){
            field = value
            p.color = value
            invalidate()
        }

    var iconResources = "https://i.natgeofe.com/k/c02b35d2-bfd7-4ed9-aad4-8e25627cd481/komodo-dragon-head-on_2x1.jpg"
        set(value){
            field = value
            getVectorBitmap()
        }
    var bitmap: Bitmap? = null
        set(value) {
            field = value
        }

    private var animator: ValueAnimator? = null
    private var radius = 0f
    private var xTouch = 0f;
    private var yTouth = 0f;
    private var rippleRadius = 0f;
    private var isAmim = false
    private var mOnClickListener: OnClickListener? = null

    init{
            context.withStyledAttributes(attrs,R.styleable.BesieImage){
            pColor = getColor(R.styleable.BesieImage_bi_background_color,
                ContextCompat.getColor(context, R.color.ui_gray))
            rippleColor = getColor(R.styleable.BesieImage_bi_ripple_color,
                ContextCompat.getColor(context, android.R.color.transparent))
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
    override fun onDraw(canvas: Canvas) {
        try{
            this.canvas = canvas
            drawBesie()
            super.onDraw(canvas);
            //if(iconResources.isNotEmpty()) getVectorBitmap()
            if(isAmim){
                drawRipple()
            }
        }catch (e: Exception){
            Log.d(TAG, e.toString())
        }
    }

    private fun drawBesie() {
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

        canvas!!.clipPath(path)
        canvas!!.drawPath(path, p);
    }

    private fun drawRipple(){
        canvas!!.drawCircle(xTouch, yTouth, rippleRadius, pRipple!!);
        if(rippleRadius >= widthView){
            isAmim = false
            invalidate()
        }
    }
    private fun drawIcon(bm: Bitmap?){
       try {
           bm?.let {
               canvas!!.drawBitmap(
                   it,
                   widthView.toFloat(),
                   widthView.toFloat(), Paint())
           }
       }catch (e: Exception){
           Log.d(TAG, "${e.message}")
       }
    }

    private fun getVectorBitmap(){
//        with(context).asBitmap().load(iconResources).into(object : CustomTarget<Bitmap?>(width, height) {
//            @RequiresApi(Build.VERSION_CODES.S)
//            override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
//                drawIcon(resource)
//            }
//            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
//        })

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