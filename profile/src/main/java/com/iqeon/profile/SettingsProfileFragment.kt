package com.iqeon.profile

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.iqeon.profile.databinding.FragmentSettingsProfileBinding
import com.petsvote.data.UserInfo
import com.petsvote.ui.navigation.UserNavigation
import com.petsvote.ui.openUrl
import com.petsvote.ui.ratingApp
import com.petsvote.ui.sendSupport
import com.petsvote.ui.shareApp
import me.vponomarenko.injectionmanager.x.XInjectionManager

class SettingsProfileFragment: DialogFragment(R.layout.fragment_settings_profile) {

    private val urlIG = "https://www.instagram.com/petsvote.app/"
    private val urlFB = "https://www.facebook.com/petsvotepage/"
    private val urlTW = "https://twitter.com/petsvotea"
    private val urlTG = "https://t.me/petsvote"
    private val urlVB = "https://invite.viber.com/?g2=AQAYi1kZYJYNZE59qwGSzxMKpTqMpvh0FXKzRpQ43X7jCovo0JYtv3nxsgmeu9dA"

    private val navigationUser: UserNavigation by lazy {
        XInjectionManager.findComponent<UserNavigation>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, com.petsvote.ui.R.style.MyDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSettingsProfileBinding.bind(view)

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.instagram.setOnClickListener {
            openUrl(urlIG)
        }
        binding.facebook.setOnClickListener {
            openUrl(urlFB)
        }

        binding.twitter.setOnClickListener {
            openUrl(urlTW)
        }

        binding.telegram.setOnClickListener {
            openUrl(urlTG)
        }

        binding.viber.setOnClickListener {
            openUrl(urlVB)
        }

        binding.supportContainer.setOnClickListener {
            it.isPressed = true
            sendSupport()
        }

        binding.ratingContainer.setOnClickListener {
            it.isPressed = true
            ratingApp()
        }

        binding.profileContainer.setOnClickListener {
            it.isPressed = true
            activity?.let { it1 -> navigationUser.startUserProfile(it1) }
        }

        binding.shareContainer.setOnClickListener {
            it.isPressed = true
            shareApp()
        }

        binding.switchNotify.isChecked = UserInfo.getNotify(requireContext())
        binding.switchNotify.setOnCheckedChangeListener { compoundButton, b ->
            UserInfo.setNotify(requireContext(), b)
        }

    }

    override fun onResume() {
        super.onResume()
        if (dialog == null) return
        val window = dialog!!.window ?: return
        val width = resources.displayMetrics.widthPixels - resources.displayMetrics.density * 16
        val params: WindowManager.LayoutParams = window.attributes
        params.width = width.toInt()
        window.attributes = params
    }
}