package com.petsvote.ui

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context.WINDOW_SERVICE
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi


class TopBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val TAG = TopBar::class.java.name
    private var widthView = 0
    private var heightView = 0

    private lateinit var ratingContainer: ConstraintLayout
    private lateinit var starContainer: ConstraintLayout
    private lateinit var profileContainer: ConstraintLayout

    private lateinit var rating: ImageView
    private lateinit var star: ImageView
    private lateinit var profile: ImageView

    private lateinit var scroll: HorizontalScrollView

    private var ratingLayoutParams: LinearLayout.LayoutParams? = null
    private var starLayoutParams: LinearLayout.LayoutParams? = null
    private var profileLayoutParams: LinearLayout.LayoutParams? = null

    init {

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var root = inflater.inflate(R.layout.top_bar, this, true)
        val windowManager: WindowManager? = context.getSystemService(WINDOW_SERVICE) as WindowManager?
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        widthView = (width / displayMetrics.density).toInt()
        heightView = (height / displayMetrics.density).toInt()

        var tabItemWidth = (widthView - 32) /3

        Log.d(TAG, "Screen Height = $height, width = $width")
        Log.d(TAG, "Screen in DP Height = $heightView, width = $widthView")
        Log.d(TAG, "TabItem width margin  = ${(widthView / 2  - 16).toFloat()}")
        Log.d(TAG, "TabItem width = ${((width / 2) - (32 * displayMetrics.density)).toInt()}")

        ratingContainer = root.findViewById<ConstraintLayout>(R.id.rating)
        starContainer = root.findViewById<ConstraintLayout>(R.id.star)
        profileContainer = root.findViewById<ConstraintLayout>(R.id.profile)

        rating = root.findViewById(R.id.rating_icon)
        star = root.findViewById(R.id.star_icon)
        profile = root.findViewById(R.id.profile_icon)

        scroll = root.findViewById(R.id.scroll)

        ratingContainer.margin(
            (widthView / 2  - 16).toFloat()
        )
        ratingLayoutParams = ratingContainer.layoutParams as LinearLayout.LayoutParams?
        starLayoutParams = starContainer.layoutParams as LinearLayout.LayoutParams?
        profileLayoutParams = profileContainer.layoutParams as LinearLayout.LayoutParams?

        ratingLayoutParams?.width = ((width / 2) - (32 * displayMetrics.density)).toInt()
        starLayoutParams?.width = ((width / 2) - (32 * displayMetrics.density)).toInt()
        profileLayoutParams?.width = ((width / 2) - (32 * displayMetrics.density)).toInt()


        initView()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun initView(){
        star.setOnClickListener {
            scroll.scrollTo(500, 0)
        }

        rating.setOnClickListener {
            scroll.scrollTo(0, 0)
        }

        profile.setOnClickListener {
            scroll.scrollTo(1000, 0)
        }
    }

    fun ImageView.viewToCenter(){
        val params = this.layoutParams as ConstraintLayout.LayoutParams
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
        params.width = width /3
    }

}