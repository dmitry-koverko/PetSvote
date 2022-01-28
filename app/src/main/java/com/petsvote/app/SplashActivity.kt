package com.petsvote.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.petsvote.register.RegisterFragment
import com.petsvote.splash.SplashFragment

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, SplashFragment())
            commit()
        }
    }
}