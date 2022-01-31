package com.petsvote.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.petsvote.filter.fragments.SelectCityFragment
import com.petsvote.filter.fragments.SelectCountryFragment

class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        var id = intent.getIntExtra("id", 1)
        var fragment: Fragment = if(id?.toInt() == 1) SelectCityFragment() else SelectCountryFragment()

        supportFragmentManager.commit {
            replace(R.id.container, fragment)
            setReorderingAllowed(true)
        }

    }

}