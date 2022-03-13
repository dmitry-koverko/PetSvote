package com.iqeon.profile.simple

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iqeon.profile.R
import com.iqeon.profile.SettingsProfileFragment
import com.iqeon.profile.UPViewModel
import com.iqeon.profile.UserProfileFragment
import com.iqeon.profile.databinding.FragmentSimpleUserProfileBinding
import com.iqeon.profile.di.UserProfileComponentViewModel
import com.petsvote.api.entity.Breed
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.UserPets
import com.petsvote.data.UserInfo
import com.petsvote.ui.dialogs.InformationPhotoDialog
import com.petsvote.ui.dialogs.SelectPhotoDialog
import com.petsvote.ui.navigation.PetNavigation
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class SimpleUserProfileFragment: Fragment(R.layout.fragment_simple_user_profile) {

    private val TAG = SimpleUserProfileFragment::class.java.name
    private var settingsDialog = SettingsProfileFragment()

    @Inject
    internal lateinit var supViewModelFactory: Lazy<SimpleUPViewModel.Factory>

    private val UPCViewModel: UserProfileComponentViewModel by viewModels()
    private val viewModel: SimpleUPViewModel by viewModels {
        supViewModelFactory.get()
    }

    private val navigationPet: PetNavigation by lazy {
        XInjectionManager.findComponent<PetNavigation>()
    }

    private var listPets = mutableListOf<UserPets>()
    private var adapter: MyPetAdapter? = null
    private var petsAdapter = MyPetAdapter(listPets)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var binding = FragmentSimpleUserProfileBinding.bind(view)

        this.adapter = petsAdapter
        binding.userPets.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = petsAdapter
        }

        binding.addPet.setOnClickListener { activity?.let { it1 -> navigationPet.toAddPet(it1) } }

        lifecycleScope.launchWhenStarted {
            viewModel.uiUser.collect {
                if(it.isNotEmpty()) petsAdapter.updateList(it as MutableList<UserPets>)
            }
        }

        binding.profileContainer.setOnClickListener {
            try {
                settingsDialog.show(childFragmentManager, "settingsDialog")
            }catch (e: Exception){}
        }

        viewModel.getUserPets()
        Log.d(TAG, "bearer = ${UserInfo.getBearer(requireContext())}")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        UPCViewModel.ratingComponent.injectSimple(this)
    }

}