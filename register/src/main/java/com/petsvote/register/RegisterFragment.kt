package com.petsvote.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.petsvote.register.databinding.FragmentRegisterBinding
import com.petsvote.register.di.RegisterComponentViewModel
import com.petsvote.ui.navigation.RegisterNavigation
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import me.vponomarenko.injectionmanager.x.XInjectionManager

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val navigation: RegisterNavigation by lazy {
        XInjectionManager.findComponent<RegisterNavigation>()
    }

    @Inject
    internal lateinit var registerViewModelFactory: Lazy<RegisterViewModel.Factory>

    private val registerComponentViewModel: RegisterComponentViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels{
        registerViewModelFactory.get()
    }

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int= 123

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentRegisterBinding.bind(view)

        binding.legalBl.setOnClickListener {
            navigation.toLegal()
        }

        binding.register.setOnClickListener {
            signInGoogle()
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.uiState.collect { uiState ->
               if(uiState) navigation.closeRegister()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterFragment()
    }

    private  fun signInGoogle(){

        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }
    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }
    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                saveAccount(account)
            }
        } catch (e:ApiException){
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAccount(account: GoogleSignInAccount) {
        account.id?.let { registerViewModel.getCurrensies(it) }
    }

    override fun onStart() {
        super.onStart()
        if(context?.let { GoogleSignIn.getLastSignedInAccount(it) } !=null){
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= context?.let { GoogleSignIn.getClient(it,gso) }!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerComponentViewModel.registerComponent.inject(this)
    }

}