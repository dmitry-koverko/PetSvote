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
        var countryId = intent.getIntExtra("countryId", 0)
        var fragment: Fragment =
            if(id == 1) SelectCityFragment(countryId) else SelectCountryFragment()

        supportFragmentManager.commit {
            replace(R.id.container, fragment)
            setReorderingAllowed(true)
        }

    }

}