package com.petsvote.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout

class BesieKeyboard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var mBesieKeyboardListener: BesieKeyboardListener? = null

    init {
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.besie_keyboard, this, true)

        var oneContainer = findViewById<BesieLayout>(R.id.one_container)
        var one = findViewById<SFTextView>(R.id.one)
        one.setOnClickListener {
            oneContainer.animRipple()
        }
        oneContainer.setOnClickListener {
            mBesieKeyboardListener?.click(1)
        }

        var twoContainer = findViewById<BesieLayout>(R.id.two_container)
        var two = findViewById<SFTextView>(R.id.two)
        two.setOnClickListener {
            twoContainer.animRipple()
        }
        twoContainer.setOnClickListener {
            mBesieKeyboardListener?.click(2)
        }

        var fourContainer = findViewById<BesieLayout>(R.id.four_container)
        var four = findViewById<SFTextView>(R.id.four)
        four.setOnClickListener {
            fourContainer.animRipple()
        }
        fourContainer.setOnClickListener {
            mBesieKeyboardListener?.click(4)
        }

        var threeContainer = findViewById<BesieLayout>(R.id.three_container)
        var three = findViewById<SFTextView>(R.id.three)
        three.setOnClickListener {
            threeContainer.animRipple()
        }
        threeContainer.setOnClickListener {
            mBesieKeyboardListener?.click(3)
        }

        var fiveContainer = findViewById<BesieLayout>(R.id.five_container)
        var five = findViewById<SFTextView>(R.id.five)
        five.setOnClickListener {
            fiveContainer.animRipple()
        }
        fiveContainer.setOnClickListener {
            mBesieKeyboardListener?.click(5)
        }

        var sixContainer = findViewById<BesieLayout>(R.id.six_container)
        var six = findViewById<SFTextView>(R.id.six)
        six.setOnClickListener {
            sixContainer.animRipple()
        }
        sixContainer.setOnClickListener {
            mBesieKeyboardListener?.click(6)
        }

        var sevenContainer = findViewById<BesieLayout>(R.id.seven_container)
        var seven = findViewById<SFTextView>(R.id.seven)
        seven.setOnClickListener {
            sevenContainer.animRipple()
        }
        sevenContainer.setOnClickListener {
            mBesieKeyboardListener?.click(7)
        }

        var eightContainer = findViewById<BesieLayout>(R.id.eight_container)
        var eight = findViewById<SFTextView>(R.id.eight)
        eight.setOnClickListener {
            eightContainer.animRipple()
        }
        eightContainer.setOnClickListener {
            mBesieKeyboardListener?.click(8)
        }

        var nineContainer = findViewById<BesieLayout>(R.id.nine_container)
        var nine = findViewById<SFTextView>(R.id.nine)
        nine.setOnClickListener {
            nineContainer.animRipple()
        }
        nineContainer.setOnClickListener {
            mBesieKeyboardListener?.click(9)
        }

        var zeroContainer = findViewById<BesieLayout>(R.id.zero_container)
        var zero = findViewById<SFTextView>(R.id.zero)
        zero.setOnClickListener {
            zeroContainer.animRipple()
        }
        zeroContainer.setOnClickListener {
            mBesieKeyboardListener?.click(0)
        }

        var clearContainer = findViewById<BesieLayout>(R.id.clear_container)
        var clear = findViewById<ImageView>(R.id.clear)
        clear.setOnClickListener {
            clearContainer.animRipple()
        }
        clearContainer.setOnClickListener {
            mBesieKeyboardListener?.click(-1)
        }

    }

    fun setOnKeyboardListener(besieKeyboardListener: BesieKeyboardListener){
        mBesieKeyboardListener = besieKeyboardListener
    }

    interface BesieKeyboardListener{
        fun click(value: Int)
    }

}