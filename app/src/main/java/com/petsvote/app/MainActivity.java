package com.petsvote.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.petsvote.legal.InfoTermsFragment;
import com.petsvote.legal.LegalFragment;
import com.petsvote.register.RegisterFragment;
import com.petsvote.splash.SplashFragment;
import com.petsvote.vote.VoteFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new VoteFragment())
                    .commitNow();
        }
    }
}