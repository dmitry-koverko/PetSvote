package com.petsvote.splash

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.petsvote.data.UserInfo
import com.petsvote.splash.databinding.FragmentSplashBinding
import com.petsvote.splash.di.SplashComponentViewModel
import com.petsvote.ui.navigation.RegisterNavigation
import com.petsvote.ui.navigation.TabsNavigation
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import me.vponomarenko.injectionmanager.x.XInjectionManager

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val navigationRegister: RegisterNavigation by lazy {
        XInjectionManager.findComponent<RegisterNavigation>()
    }

    private val navigationTabs: TabsNavigation by lazy {
        XInjectionManager.findComponent<TabsNavigation>()
    }


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

        val resources: Resources? = activity?.resources
        val config: Configuration? = resources?.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var lang = config?.locales?.get(0)?.language?.toString()!!
            Log.d(TAG, "language user = $lang")
            Log.d(TAG, "bearer user = ${UserInfo.getBearer(requireContext())}")
            if(UserInfo.listLanguage.contains(lang)) {
                UserInfo.languge = lang
                UserInfo.setLanguage(requireContext(), lang)
            }
        }
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
                if(uiState == 2){
                    var bearer = context?.let { UserInfo.getBearer(it) } ?: ""
                    if(bearer.isNotEmpty()) navigationTabs.startTabsNavigation()
                    else navigationRegister.startRegisterNavigation()
                }
            }
        }
    }

    private fun setUIProgress(){
        binding!!.icon.visibility = View.GONE
        binding!!.progressBar.visibility = View.VISIBLE
        splashViewModel.getConfig(UserInfo.getLanguage(requireContext()))

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        splashComponentViewModel.splashComponent.inject(this)
    }

}