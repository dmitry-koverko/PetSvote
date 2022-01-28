package com.petsvote.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import com.petsvote.ui.parallax.DotIndicator
import java.util.zip.Inflater

class Button @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var dot: DotIndicator
    private var mOnClickListener: OnClickListener? = null

    private var widthV = 0
        set(value) {
            field = value
            updateDot()
        }

    private var heightV = 0

    init{
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.button_text, this, true)

        dot = findViewById<DotIndicator>(R.id.dot)

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                widthV = this@Button.width
                heightV = this@Button.height

                Log.d("BUTTON", "with = ${this@Button.width}")
                Log.d("BUTTON", "with = ${this@Button.height}")

                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })

        var bl = findViewById<BesieLayout>(R.id.bl)
        bl.setOnClickListener {
            mOnClickListener?.onClick(this)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mOnClickListener?.onClick(this)
            }
        }
        return true
    }

    private fun updateDot() {
        //dot.layoutParams = LayoutParams(width, height)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }

}