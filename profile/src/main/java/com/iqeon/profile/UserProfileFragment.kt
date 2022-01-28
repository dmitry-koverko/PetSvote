package com.iqeon.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.iqeon.profile.databinding.FragmentUserProfileBinding
import com.iqeon.profile.di.UserProfileComponentViewModel
import com.petsvote.api.entity.User
import com.petsvote.ui.loadImage
import com.petsvote.ui.navigation.TabsNavigation
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    @Inject
    internal lateinit var upViewModelFactory: Lazy<UPViewModel.Factory>

    private val UPCViewModel: UserProfileComponentViewModel by viewModels()
    private val viewModel: UPViewModel by viewModels {
        upViewModelFactory.get()
    }

    private val navigationTabs: TabsNavigation by lazy {
        XInjectionManager.findComponent<TabsNavigation>()
    }

    private lateinit var binding: FragmentUserProfileBinding
    private var userUI = User(null,null, null, null,
        null, null, null, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserProfileBinding.bind(view)

        lifecycleScope.launchWhenStarted {
            viewModel.uiUser.collect { user ->

                //user.avatar?.let { binding.avatar.loadImage(it) }
                user.first_name?.let { binding.username.setText(it) }
                user.last_name?.let {binding.lastname.setText(it)}

                userUI = user

            }
        }

        binding.countryTitle.setOnClickListener{
            binding.selectCountry.performClick()
        }
        binding.country.setOnClickListener{
            binding.selectCountry.performClick()
        }
        binding.selectCountry.setOnClickListener {
           startSelect(2)
        }

        binding.cityTitle.setOnClickListener{
            binding.selectCity.performClick()
        }
        binding.city.setOnClickListener{
            binding.selectCity.performClick()
        }
        binding.selectCity.setOnClickListener {
            startSelect(1)
        }

        binding.username.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                userUI.first_name = p0.toString()
                checkData()
            }

        })

        binding.lastname.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                userUI.last_name = p0.toString()
                checkData()
            }

        })

        viewModel.getUserInfo()
    }

    fun startSelect(state: Int){
        object : CountDownTimer(300, 300) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                activity?.let { it1 -> navigationTabs.startSelectActivity(state, it1) }
            }
        }.start()
    }

    private fun checkData(){
        if(userUI.first_name.isNullOrEmpty()) return
        if (userUI.last_name.isNullOrEmpty()) return

        binding.save.setOnClickListener {
            viewModel.saveUserInfo(userUI)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UPCViewModel.ratingComponent.inject(this)
    }


}