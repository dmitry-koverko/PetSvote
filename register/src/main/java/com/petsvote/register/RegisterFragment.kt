package com.petsvote.register

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.petsvote.data.DocumentsInfo
import com.petsvote.data.UserInfo
import com.petsvote.register.databinding.FragmentRegisterBinding
import com.petsvote.register.di.RegisterComponentViewModel
import com.petsvote.ui.navigation.RegisterNavigation
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val TAG = RegisterFragment::class.java.name

    private val navigation: RegisterNavigation by lazy {
        XInjectionManager.findComponent<RegisterNavigation>()
    }

    @Inject
    internal lateinit var registerViewModelFactory: Lazy<RegisterViewModel.Factory>

    private val registerComponentViewModel: RegisterComponentViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels{
        registerViewModelFactory.get()
    }


    private lateinit var binding: FragmentRegisterBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN:Int= 123

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterBinding.bind(view)

        val resources: Resources? = activity?.resources
        val config: Configuration? = resources?.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var lang = config?.locales?.get(0)?.language?.toString()!!
            UserInfo.setLanguage(requireContext(), lang)
            registerViewModel.getPolicy(lang)
            registerViewModel.getTerms(lang)
        }

        binding.legalBl.setOnClickListener {
            navigation.toLegal()
        }

        binding.register.setOnClickListener {
            signInGoogle()
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.uiState.collect { uiState ->
               if(uiState) {
                   binding.progress.visibility = View.GONE
                   navigation.closeRegister()
               }
            }
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.uiStatePolicy.collect {
                DocumentsInfo.setPolicy(requireContext(), it)
            }
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.uiStateTerms.collect {
                DocumentsInfo.setTerms(requireContext(), it)
            }
        }
        registerViewModel.getBreeds()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterFragment()
    }

    private  fun signInGoogle(){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                saveAccount(account)
            }
        } catch (e:ApiException){
            //firebaseAuthWithGoogle()
            registerViewModel.getCurrensies("123")
            //Toast.makeText(context, e.message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAccount(account: GoogleSignInAccount) {
        account.id?.let {
            binding.legalContainer.visibility = View.GONE
            binding.containerMiddle.visibility = View.GONE
            binding.containerBottom.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            registerViewModel.getCurrensies(it)
        }
    }

    override fun onStart() {
        super.onStart()
        if(context?.let { GoogleSignIn.getLastSignedInAccount(it) } != null){
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= activity?.let { GoogleSignIn.getClient(it, gso) }!!;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerComponentViewModel.registerComponent.inject(this)
    }

}