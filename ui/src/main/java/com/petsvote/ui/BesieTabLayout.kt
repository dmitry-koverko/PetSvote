package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes

class BesieTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val TAG = BesieTabLayout::class.java.name
    var tabWith = 0
        set(value){
            field = value
        }
    var tabHeight = 0
    var animTranslationX = 0f
    private var animator: ValueAnimator? = null

    private var tab1: BesieTab
    private var tab2: BesieTab
    private var tab3: BesieTab
    private var tabActive: BesieTabActive

    private var currentTab = R.id.tab1

    init {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var root = inflater.inflate(R.layout.besie_tab_layout, this, true)
        tab1 = root.findViewById(R.id.tab1)
        tab2 = root.findViewById(R.id.tab2)
        tab3 = root.findViewById(R.id.tab3)
        tabActive = root.findViewById(R.id.tab_active)

        getCurrentTabActive()

        initListener()
    }

    fun getCurrentTabActive(){
        Log.d(TAG, "current position active tab = ${tabActive.translationX}")
        if(tabActive.translationX <= tabWith / 4 ){
            tabActive.textTab = tab1.textTab
        }else if(tabActive.translationX > tabWith/4 &&
                tabActive.translationX < tabWith * 1.5){
            tabActive.textTab = tab2.textTab
        }else if(tabActive.translationX >= tabWith * 1.5){
            tabActive.textTab = tab3.textTab
        }
    }

    private fun initListener() {
        tab1.setOnClickListener {
            if(currentTab != it.id) {
                animMove(animTranslationX, 0f)
                currentTab = it.id
            }
        }

        tab2.setOnClickListener {
            if(currentTab != it.id) {
                animMove(animTranslationX, tabWith.toFloat())
                currentTab = it.id
            }
        }

        tab3.setOnClickListener {
            if(currentTab != it.id) {
                animMove(animTranslationX, tabWith * 2.toFloat())
                currentTab = it.id
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        tabWith = w/ 3
        tabHeight = h
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun animMove(val1: Float, val2: Float){
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_MOVE", val1, val2)

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(200)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            animTranslationX = animation.getAnimatedValue("PROPERTY_MOVE") as Float
            tabActive.translationX = animTranslationX
            getCurrentTabActive()
        })
        animator!!.start()
    }

}

