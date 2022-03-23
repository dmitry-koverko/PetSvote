package com.petsvote.pet.addpet

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.petsvote.pet.R
import com.petsvote.pet.databinding.DialogLoginInstaBinding
import com.petsvote.pet.databinding.FragmentAddPetBinding
import com.petsvote.pet.di.PetComponentViewModel
import com.petsvote.ui.dialogs.BaseDialog
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoginInstaDialog: BaseDialog(R.layout.dialog_login_insta) {

    private var TAG = LoginInstaDialog::class.java.name
    private var mLoginInstaDialogListener: LoginInstaDialogListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = DialogLoginInstaBinding.bind(view)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true

        CookieManager.getInstance().removeAllCookies {  }

        var wvClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                val cookie: String = CookieManager.getInstance().getCookie(url)
                if (cookie == null || !cookie.contains("sessionid")) {
                    val cookies = CookieManager.getInstance().getCookie(url)
                    Log.d(TAG, "All the cookies in a string:$cookies")
                    binding.webView.setVisibility(View.VISIBLE)
                } else {
                    binding.webView.loadUrl("https://www.instagram.com/petsvote.app/")
                    CookieSyncManager.getInstance().sync()
                    val cookies = CookieManager.getInstance().getCookie(url)
                    Log.d(TAG, "All the cookies in a string:$cookies")
                    val ds_user_id =
                        cookie.split("ds_user_id=").toTypedArray()[1].split(";").toTypedArray()[0]
                    var id = ds_user_id.toLongOrNull()
                    mLoginInstaDialogListener?.setUsername(id)
                    binding.webView.destroy()
                    dismiss()
                    return
                }
                super.onPageFinished(view, url)
            }
        }


        binding.webView.webViewClient = wvClient
        binding.webView.loadUrl("https://www.instagram.com/petsvote.app/")
    }

    fun setLoginInstaDialogListener(listener: LoginInstaDialogListener){
        mLoginInstaDialogListener = listener
    }

    interface LoginInstaDialogListener{
        fun setUsername(userId: Long?)
    }

}