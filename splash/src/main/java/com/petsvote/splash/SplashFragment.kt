package com.petsvote.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.petsvote.base.BaseFragment
import com.petsvote.splash.databinding.FragmentSplashBinding
import com.petsvote.splash.di.SplashComponentViewModel
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.flow.collect

class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    internal lateinit var splashViewModelFactory: Lazy<SplashViewModel.Factory>

    private val splashComponentViewModel: SplashComponentViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels {
        splashViewModelFactory.get()
    }

    private var binding: FragmentSplashBinding? = null
    private val TAG = SplashFragment::class.java.name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSplashBinding.bind(view)
        setUIStart()
    }

    private fun setUIStart(){
        object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                setUIProgress()
            }
        }.start()

        lifecycleScope.launchWhenStarted {
            splashViewModel.uiState.collect { uiState ->
                Log.d(TAG, uiState.toString())
                if(uiState.globalConfig && uiState.localizationData){
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
                }
            }
        }
    }

    private fun setUIProgress(){
        binding!!.icon.visibility = View.GONE
        binding!!.progressBar.visibility = View.VISIBLE

        splashViewModel.getConfig()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        splashComponentViewModel.splashComponent.inject(this)
    }

}