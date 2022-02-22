package com.petsvote.rating

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.PetRating
import com.petsvote.data.FilterPetsObject
import com.petsvote.data.UserInfo
import com.petsvote.filter.fragments.MenuFilterFragment
import com.petsvote.rating.adapter.MyPetRatingAdapter
import com.petsvote.rating.adapter.RatingPetAdapter
import com.petsvote.rating.databinding.FragmentRatingBinding
import com.petsvote.rating.di.RatingComponentViewModel
import com.petsvote.room.UserPets
import com.petsvote.ui.BesieTabLayoutSelectedListener
import com.petsvote.ui.BesieTabSelected
import com.petsvote.ui.list.RecyclerViewLoadMoreScroll
import com.petsvote.ui.loadImage
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatingFragment : Fragment(R.layout.fragment_rating), RatingPetAdapter.OnClickItemListener,
    BesieTabLayoutSelectedListener, MyPetRatingAdapter.OnClickItemListener {

    private val TAG = RatingFragment::class.java.name

    private var listPet = mutableListOf<PetRating>()
    private var listPetUser = mutableListOf<UserPets>()
    private var ratingPetAdapter = RatingPetAdapter(listPet)
    private var userPetAdapter = MyPetRatingAdapter(listPetUser)

    lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private var topLinearHeight = 0
    private var topLinearContainerHeight = 0
    private var filterContainerHeight = 0

    private var cardHeight = 0
    private var loadMore = false
    private var clickMyPet = 0
    @Inject
    internal lateinit var ratingViewModelFactory: Lazy<RatingViewModel.Factory>

    private val ratingComponentViewModel: RatingComponentViewModel by viewModels()
    private val ratingViewModel: RatingViewModel by viewModels {
        ratingViewModelFactory.get()
    }

    var binding: FragmentRatingBinding? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "userId = ${UserInfo.getUserId(requireContext())}")

        binding = FragmentRatingBinding.bind(view)

        Log.d(TAG, "user id = ${UserInfo.getUserId(requireContext())} user bearer =${UserInfo.getBearer(requireContext())}")

        var mLayoutManager = GridLayoutManager(context, 2)
        binding!!.listRating.apply {
            layoutManager = mLayoutManager
            this.adapter = ratingPetAdapter
        }

        var mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding!!.listPetsUser.apply {
            layoutManager = mLinearLayoutManager
            this.adapter = userPetAdapter
        }

        ratingPetAdapter.setOnClickItemListener(this)

        binding!!.listRating.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy <= 0) return

                var lastVisibleItem =
                    (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
                var totalItemCount = mLayoutManager.itemCount

                Log.d(TAG, "totalItemCount = $totalItemCount ### lastVisibleItem = $lastVisibleItem")

            }
        })

        binding!!.refresh.setColorSchemeResources(R.color.ui_primary)
        binding!!.refresh.setOnRefreshListener { binding!!.refresh.isRefreshing = false }


        binding!!.topLinear.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(topLinearHeight == 0)
                    topLinearHeight = binding!!.topLinear.measuredHeight
                binding!!.topLinear.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        binding!!.topLinearContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(topLinearContainerHeight == 0)
                    topLinearContainerHeight = binding!!.topLinearContainer.measuredHeight
                binding!!.topLinearContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        binding!!.filterContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(filterContainerHeight == 0)
                    filterContainerHeight = binding!!.filterContainer.measuredHeight
                binding!!.filterContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding!!.nestedScroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.d(TAG, "linTopHeight = $topLinearHeight \n scrollY = $scrollY  ### w = ${binding!!.listRating.height}")
            var loadY = cardHeight * 5
            Log.d("RatingFragment", "heightRecycler = $loadY")
//            if(scrollY >= binding.listRating.height - 10000) {
//                loadMore()
//            }

            var a = (binding!!.nestedScroll as ViewGroup).getChildAt(0) as ViewGroup
            Log.d("TAGG", "measuredHeight = ${a.getChildAt(a.getChildCount() - 1).getMeasuredHeight()}" +
                    "scrollY = $scrollY ### oldScrollY = $oldScrollY")

            Log.d("RatingFragment", "listSize = ${listPet.size}")

            if(a.getChildAt(a.getChildCount() - 1) != null) {
                if ((scrollY >= (a.getChildAt(a.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                    scrollY > oldScrollY) {
                    loadMore()

                    //code to fetch more data for endless scrolling
                }
            }

            var lpTopLinear = binding!!.topLinear.layoutParams
            var s = topLinearHeight - scrollY / 2
            if (s < 0) return@setOnScrollChangeListener
            lpTopLinear.height = s
            binding!!.topLinear.layoutParams = lpTopLinear
        }

        lifecycleScope.launchWhenStarted {
            ratingViewModel.uiState.collect { uiState ->
                loadMore = false
                if(uiState.isNullOrEmpty()) return@collect
                var newList = mutableListOf<PetRating>()
                newList = uiState as MutableList<PetRating>
                binding!!.refresh.isRefreshing = false
                if(binding!!.topPet.root.visibility == View.GONE){
                    var topPet = uiState[0]
                    binding!!.topPet.root.visibility = View.VISIBLE
                    if(!topPet.photos.isNullOrEmpty())
                        binding!!.topPet.image.loadImage(uiState[0].photos[0].url)
                    binding!!.topPet.name.text = topPet.name
                    binding!!.topPet.location.text = "${topPet.country_name}, ${topPet.city_name}"

                    if(newList.isNotEmpty()) newList.removeAt(0)
                    loadMore()
                }

                listPet.addAll(newList)
                ratingPetAdapter.notifyDataSetChanged()
            }
        }

        binding!!.imageFilter.setOnClickListener {
            FilterPetsObject.show.value = 1
        }

        commitFilter()
        loadMore()

        lifecycleScope.launchWhenResumed {
            FilterPetsObject.show.collect {
                if(it == 1) showFilter(true)
                else if(it == 2) {
                    updateFilterText()
                    showFilter(false)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            ratingViewModel.uiStatePets.collect {
                listPetUser.addAll(it)
                userPetAdapter?.notifyDataSetChanged()
            }
        }

        lifecycleScope.launchWhenStarted {
            ratingViewModel.uiStateUserPets.collect {
                listPet.clear()
                listPet.addAll(it)
                ratingPetAdapter.notifyDataSetChanged()
                scrollToPosition()
            }
        }

        binding!!.tabs.setTabSelectedListener(this)
        when(UserInfo.getTabsFilter(requireContext())){
            1 -> binding!!.tabs.initCountryTabs()
            2 -> binding!!.tabs.initWorldTab()
        }

        userPetAdapter.setOnClickItemListener(this)

        ratingViewModel.getUserInfo()
        updateFilterText()

    }

    private fun scrollToPosition() {
        if(clickMyPet != 0){
            var indexPet = listPet.firstOrNull { it.id == clickMyPet }
            var indexArray = listPet.indexOf(indexPet)
            var scroll = indexArray / 2 * cardHeight
            binding?.nestedScroll?.scrollTo(0, scroll + 300)
            Log.d(TAG, "click pet in position ${indexArray / 3}")
        }
    }

    private fun commitFilter() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.container, MenuFilterFragment())
            commit()
        }
    }

    private fun loadMore() {
        if(!loadMore){
            ratingViewModel.getRating()
            loadMore = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ratingComponentViewModel.ratingComponent.inject(this)
    }

    override fun onClick(position: Int) {

    }

    override fun onSizeCard(width: Int, height: Int) {
        cardHeight = height
    }

    private fun showFilter(isShow: Boolean){
        val objectAnimator = if(!isShow)
            ObjectAnimator.ofFloat(binding!!.container, "translationY", binding!!.root.height.toFloat())
        else ObjectAnimator.ofFloat(binding!!.container, "translationY", 0f)
        objectAnimator.duration = 300
        objectAnimator.start()

    }

    private fun updateFilterText(){
        var txt = "Беларусь, "

        var itKinds = FilterPetsObject.listKinds.value
        if(itKinds.isEmpty()) txt += "${getString(R.string.all_kinds2).lowercase()}, "
        else if(itKinds.size == 1) txt += "${itKinds.get(0).name.lowercase()}, "
        else txt += "${getString(R.string.other_kinds).lowercase()}, "

        var itBreeds = FilterPetsObject.breed.value
        if (itBreeds.title.isNotEmpty()) {
            txt += "${itBreeds.title.lowercase()}, "
        }
        if(itBreeds.id == -1) txt += "${getString(R.string.no_breeds).lowercase()}, "
        else if(itBreeds.id == 0) txt += "${getString(R.string.all_breeds).lowercase()}, "

        var tSex = when(FilterPetsObject.sex.value.type){
            0 -> getString(R.string.sex_all2)
            1 -> getString(R.string.sex_man)
            2 -> getString(R.string.sex_girl)
            else -> getString(R.string.sex_all2)
        }

        txt += "${tSex.lowercase()}, "
        txt +=  "${FilterPetsObject.minValue.value} - ${FilterPetsObject.maxValue.value}"

        binding!!.filterText.text = txt
    }

    override fun selected(tab: BesieTabSelected) {
        UserInfo.setTabsFilter(requireContext(),
            when(tab){
                BesieTabSelected.CITY -> 0
                BesieTabSelected.COUNTRY -> 1
                else -> 2
            })

    }

    override fun onClick(pet: UserPets) {
        pet.id?.let {
            ratingViewModel.getRating(it)
            clickMyPet = it
        }
    }

    override fun onSearch() {

    }

}