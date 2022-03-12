package com.petsvote.ui

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
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
        inflater.inflate(R.layout.besie_layout, this@BesieLayout, true)


        context.withStyledAttributes(attrs, R.styleable.BesieLayout){
            findViewById<DotIndicator>(R.id.dot).apply {
                isRipple = getBoolean(R.styleable.BesieLayout_bl_ripple, false)
                setOnClickListener{
                    mOnClickListener?.onClick(this)
                }
                pRipple.color = getColor(R.styleable.BesieLayout_bl_ripple_color,
                    ContextCompat.getColor(context, R.color.ripple_gray))
                paint.color = getColor(R.styleable.BesieLayout_bl_background,
                    ContextCompat.getColor(context, android.R.color.transparent))
                changeLayout = getBoolean(R.styleable.BesieLayout_bl_change_layout, true)

            }
            dotColor = getColor(R.styleable.BesieLayout_bl_background,
                ContextCompat.getColor(context, android.R.color.transparent))
            dotColor()
        }

        rootView.viewTreeObserver.addOnGlobalFocusChangeListener(object: ViewTreeObserver.OnGlobalLayoutListener,
            ViewTreeObserver.OnGlobalFocusChangeListener {
            override fun onGlobalLayout() {
                Log.d("BESIE LAYOUT", "width = $width height = $height")
            }

            override fun onGlobalFocusChanged(p0: View?, p1: View?) {
            }

        })

//        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
//            override fun onGlobalLayout() {
//
//                viewTreeObserver.removeOnGlobalLayoutListener(this)
//
//                val childView: View = getChildAt(0)
//                removeAllViews()
//
//                var inflater =
//                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                inflater.inflate(R.layout.besie_layout, this@BesieLayout, true)
//
//                var root = (findViewById<View>(R.id.root) as ConstraintLayout)
//                root.addView(childView)
//
//
//                Log.d("TAG", "${childView.width}")
//                Log.d("TAG", "${childView.height}")
//
//                findViewById<DotIndicator>(R.id.dot).apply {
//                    widthView = childView.width
//                    heightView = childView.height
//                    radius = childView.height / 2f
//                }
//
//
//            }
//
//        })

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                animRipple()
            }
        }
        return true
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
        val timer = object: CountDownTimer(200, 200) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {mOnClickListener?.onClick(this@BesieLayout)}
        }
        timer.start()
    }

    private fun dotColor(){
        findViewById<DotIndicator>(R.id.dot).setDotColor(dotColor)
    }

    fun setActive(value: Boolean){
        findViewById<DotIndicator>(R.id.dot).apply {
            isRipple = value
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("BESIE LAYOUT", "left = $left  right = $right top = $top bottom = $bottom")
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }

}