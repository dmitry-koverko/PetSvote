package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
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
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

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

     var tab1: SFTextView
     var tab2: SFTextView
     var tab3: SFTextView

    private var tabIndicator: BesieLayout

    private var currentTab = R.id.text_city
    private var mBesieTabLayoutSelectedListener: BesieTabLayoutSelectedListener? = null

    private lateinit var sex_allS: String
    private lateinit var sex_manS: String
    private lateinit var sex_girlS: String

    var initCountryWorld = 0

    var type_tabs = 0

    init {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var root = inflater.inflate(R.layout.besie_tab_layout, this, true)


        context.withStyledAttributes(attrs, R.styleable.BesieTabLayout){
            type_tabs = getInt(R.styleable.BesieTabLayout_btl_type, 0)
        }

        sex_allS = context.getString(R.string.sex_all)
        sex_girlS = context.getString(R.string.sex_girl)
        sex_manS = context.getString(R.string.sex_man)

        tab1 = root.findViewById(R.id.text_city)
        tab2 = root.findViewById(R.id.text_country)
        tab3 = root.findViewById(R.id.text_world)

        tab1.animation = true
        tab2.animation = true
        tab3.animation = true

        tabIndicator = root.findViewById(R.id.tab_indicator)

        if(type_tabs == 1){
            tab1.text = sex_allS
            tab2.text = sex_manS
            tab3.text = sex_girlS
        }else if(type_tabs == 2){
            tab1.text = context.getString(R.string.sex_girl_one)
            tab2.text = context.getString(R.string.sex_man_one)
            tab3.text = context.getString(R.string.sex_no)
        }
        initListener()
    }

    fun initWorldTab(){
        if(tabWith == 0) {
            initCountryWorld = 2
            return
        }
        Log.d(TAG, "initCountryTabs = $width")
        tabIndicator.translationX = tabWith * 2f
        tab1.setTextColor(ContextCompat.getColor(context, R.color.tab_text_color))
        tab3.setTextColor(ContextCompat.getColor(context, R.color.tab_text_color_active))
        currentTab = R.id.text_world
    }

    fun clickTab1(){
        if(currentTab != R.id.text_city) {
            checkCurrentTab()
            animMove(animTranslationX, 0f)
            currentTab = R.id.text_city
            animColor(tab1, true)
            when(type_tabs){
                0 -> mBesieTabLayoutSelectedListener?.selected(BesieTabSelected.CITY)
                1 -> mBesieTabLayoutSelectedListener?.selected(BesieTabSelected.ALL)
            }
        }
    }

    fun initCountryTabs(){
        if(tabWith == 0) {
            initCountryWorld = 1
            return
        }
        Log.d(TAG, "initCountryTabs = $width")
        tabIndicator.translationX = tabWith.toFloat()
        tab1.setTextColor(ContextCompat.getColor(context, R.color.tab_text_color))
        tab2.setTextColor(ContextCompat.getColor(context, R.color.tab_text_color_active))
    }

    private fun initListener() {
        tab1.setOnClickListener {
            clickTab1()
        }

        tab2.setOnClickListener {
            if(currentTab != it.id) {
                checkCurrentTab()
                animMove(animTranslationX, tabWith.toFloat())
                currentTab = it.id
                animColor(tab2, true)
                when(type_tabs){
                    0 -> mBesieTabLayoutSelectedListener?.selected(BesieTabSelected.COUNTRY)
                    1 -> mBesieTabLayoutSelectedListener?.selected(BesieTabSelected.MAN)
                }
            }
        }

        tab3.setOnClickListener {
            if(currentTab != it.id) {
                checkCurrentTab()
                animMove(animTranslationX, tabWith * 2.toFloat())
                currentTab = it.id
                animColor(tab3, true)
                when(type_tabs){
                    0 -> mBesieTabLayoutSelectedListener?.selected(BesieTabSelected.WORLD)
                    1 -> mBesieTabLayoutSelectedListener?.selected(BesieTabSelected.GIRLS)
                }
            }
        }
    }


    private fun checkCurrentTab() {
        var view = when(currentTab){
            R.id.text_city -> tab1
            R.id.text_country -> tab2
            R.id.text_world -> tab3
            else -> tab1
        }
        animColor(view, false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged")
        tabWith = w /3
        tabHeight = h
        if(initCountryWorld == 1) initCountryTabs()
        else if(initCountryWorld == 2) initWorldTab()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun animMove(val1: Float, val2: Float){
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_MOVE", val1, val2)

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(300)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            animTranslationX = animation.getAnimatedValue("PROPERTY_MOVE") as Float
            tabIndicator.translationX = animTranslationX
        })
        animator!!.start()
    }

    private fun animColor(view: SFTextView, isActive: Boolean){
        var color1 = ContextCompat.getColor(context, R.color.tab_text_color_active)
        var color2 = ContextCompat.getColor(context, R.color.tab_text_color)

        var colorFrom = if(isActive) color2 else color1
        var colorTo = if(isActive) color1 else color2

        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener { valueAnimator ->
            val fractionAnim = valueAnimator.animatedValue as Float
            view.setTextColor(ColorUtils.blendARGB(
                colorFrom,
                colorTo,
                fractionAnim
            ))
        }
        valueAnimator.start()
    }

    fun setTabSelectedListener(listener: BesieTabLayoutSelectedListener){
        mBesieTabLayoutSelectedListener = listener
    }
}

interface BesieTabLayoutSelectedListener{
    fun selected(tab: BesieTabSelected)
}

enum class BesieTabSelected{
    CITY,
    COUNTRY,
    WORLD,
    ALL,
    MAN,
    GIRLS
}



