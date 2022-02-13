package com.petsvote.pet.info

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.petsvote.api.entity.Photo
import com.petsvote.pet.R
import com.petsvote.pet.addpet.AddPetViewModel
import com.petsvote.pet.databinding.FragmentPetInfoBinding
import com.petsvote.pet.di.PetComponentViewModel
import com.petsvote.ui.loadImage
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class PetInfoFragment : Fragment(R.layout.fragment_pet_info) {

    private val TAG = PetInfoFragment::class.java.name

    @Inject
    internal lateinit var infoViewModelFactory: Lazy<PetInfoViewModel.Factory>

    private lateinit var binding: FragmentPetInfoBinding

    private val petCViewModel: PetComponentViewModel by viewModels()
    private val viewModel: PetInfoViewModel by viewModels {
        infoViewModelFactory.get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPetInfoBinding.bind(view)
        viewModel.getPetInfo(17704158)

        lifecycleScope.launchWhenStarted {
            viewModel.uiPet.collect {pet ->
                if(pet.id != -1){
                    var lString = mutableListOf<String>()
                    pet.photos?.let {list ->
                        for (i in list){
                            lString.add(i.url)
                        }
                        if(list.isNotEmpty()) binding.parallax.list = lString
                    }
                    binding.petName.text = pet.name
                    binding.petLocate.text = "${pet.city_name}, ${pet.country_name}"
                    binding.city.text = pet.city_name
                    binding.country.text = pet.country_name
                    binding.petId.text = pet.pet_id.toString()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiPetDetails.collect {details ->
                if(details.first_name.isNotEmpty()){
                    binding.ratingBalls.text = details.global_score.toString()
                    binding.cityRating.text = details.city_range.toString() // TODO add text translate
                    binding.countryRating.text = details.country_range.toString()
                    binding.globalRating.text = details.global_range.toString()
                    binding.ownerName.text = "${details.first_name} ${details.last_name}"
                    binding.userImage.loadImage(details.avatar)
                }
            }
        }

        binding.copy.setOnClickListener {
            copyTextToClipboard()
        }

    }

    private fun copyTextToClipboard() {
        val textToCopy = binding.petId.text
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        petCViewModel.petComponent.injectInfo(this)
    }

}