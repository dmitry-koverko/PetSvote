package com.petsvote.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class SearchBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mOnTextSearchBar: OnTextSearchBar? = null
    private lateinit var edit: EditText
    var editable = true
        set(value) {
            field = value
            edit.isEnabled = value
            edit.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }

    var textSearch = ""
        set(value) {
            field = value
            edit.setText(value)
        }
    init {
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.search_bar, this, true)

        edit =  findViewById<EditText>(R.id.edit);
        edit.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                var t = p0.toString()
                if(t.isEmpty()) mOnTextSearchBar?.onClear()
                else mOnTextSearchBar?.onText(t)
            }

        })

        findViewById<BesieLayout>(R.id.clear).setOnClickListener {
            if(editable)mOnTextSearchBar?.onClear()
            else textSearch = ""
        }
    }

    fun setOnTextSearchBar(onTextSearchBar: OnTextSearchBar){
        mOnTextSearchBar = onTextSearchBar
    }

    interface OnTextSearchBar{
        fun onText(text: String)
        fun onClear()
    }
}
