package com.petsvote.rating

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.fragment.app.Fragment
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
import com.petsvote.rating.adapter.RatingAdapter
import com.petsvote.rating.adapter.RatingPetAdapter
import com.petsvote.rating.databinding.FragmentRatingBinding
import com.petsvote.rating.di.RatingComponentViewModel
import com.petsvote.room.Location
import com.petsvote.room.UserPets
import com.petsvote.ui.BesieTabLayoutSelectedListener
import com.petsvote.ui.BesieTabSelected
import com.petsvote.ui.dialogs.UserLocationDialog
import com.petsvote.ui.list.RecyclerViewLoadMoreScroll
import com.petsvote.ui.navigation.RegisterNavigation
import dagger.Lazy
import kotlinx.coroutines.flow.collect
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject


class RatingFragment : Fragment(R.layout.fragment_rating), RatingPetAdapter.OnClickItemListener,
    BesieTabLayoutSelectedListener, MyPetRatingAdapter.OnClickItemListener,
    RatingAdapter.OnClickItemListener {

    private val TAG = RatingFragment::class.java.name
    private var animator: ValueAnimator = ValueAnimator()

    private var listPet = mutableListOf<PetRating>()
    private var listPetUser = mutableListOf<UserPets>()
    private var ratingPetAdapter = RatingAdapter(listPet, this)
    private var userPetAdapter = MyPetRatingAdapter(listPetUser)

    lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private var topLinearHeight = 0
    private var topLinearContainerHeight = 0
    private var filterContainerHeight = 0

    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0

    private var cardHeight = 0
    private var loadMore = false
    private var loadTop = false
    private var clickMyPet = 0
    private var userLocation: Location? = null
    private var isShowbottomView = true
    private var isAnimationbottomView = false

    @Inject
    internal lateinit var ratingViewModelFactory: Lazy<RatingViewModel.Factory>

    private val ratingComponentViewModel: RatingComponentViewModel by viewModels()
    private val ratingViewModel: RatingViewModel by viewModels {
        ratingViewModelFactory.get()
    }

    var binding: FragmentRatingBinding? = null

    private val navigation: RegisterNavigation by lazy {
        XInjectionManager.findComponent<RegisterNavigation>()
    }

    private var topRatingPet: PetRating? = null
    private var filterRaing  = FilterRating()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "userId = ${UserInfo.getUserId(requireContext())}")

        binding = FragmentRatingBinding.bind(view)

        Log.d(TAG, "user id = ${UserInfo.getUserId(requireContext())} user bearer =${UserInfo.getBearer(requireContext())}")

        val mLayoutManager = GridLayoutManager(activity, 2)
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 2
                else -> 1
            }
        }
        binding!!.listRating.apply {
            layoutManager = mLayoutManager
            this.adapter = ratingPetAdapter
            isFocusableInTouchMode = false
        }


        var mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding!!.listPetsUser.apply {
            layoutManager = mLinearLayoutManager
            this.adapter = userPetAdapter
        }

        //ratingPetAdapter.setOnClickItemListener(this)

        binding!!.listRating.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = mLayoutManager.itemCount

                var lastVisibleItem =
                    mLayoutManager.findLastVisibleItemPosition()
                if (dy <= 0) {
                    if(isShowbottomView && !isAnimationbottomView) animBottomBar(false)
                    if (!loadTop && lastVisibleItem < 20) {
                        loadTop()
                    }
                }else {
                    if(!isShowbottomView && !isAnimationbottomView) animBottomBar(true)
                    if (!loadMore && totalItemCount <= lastVisibleItem + 20) {
                        loadMore()
                    }
                }

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
        binding!!.topLinear.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(topLinearContainerHeight == 0)
                    topLinearContainerHeight = binding!!.topLinear.measuredHeight
                binding!!.topLinear.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        binding!!.filterContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(filterContainerHeight == 0)
                    filterContainerHeight = binding!!.filterContainer.measuredHeight
                binding!!.filterContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

//        binding!!.topLinearContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
//            override fun onGlobalLayout() {
//                if(topLinearContainerHeight == 0)
//                    topLinearContainerHeight = binding!!.topLinearContainer.measuredHeight
//                binding!!.topLinearContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
//            }
//        })

//        binding!!.listRating.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            var loadY = cardHeight * listPet.size / 2
//            if (scrollY > oldScrollY && scrollY > loadY) {
//                loadMore()
//            }else if(scrollY < oldScrollY && scrollY < 2500 && scrollY > 500) loadTop()
//
//            var lpTopLinear = binding!!.topLinear.layoutParams
//            var s = topLinearHeight - scrollY / 2
//            if (s < 0) return@setOnScrollChangeListener
//            lpTopLinear.height = s
//            binding!!.topLinear.layoutParams = lpTopLinear
//        }

        lifecycleScope.launchWhenStarted {
            ratingViewModel.uiState.collect { uiState ->
                loadMore = false
                if(uiState.isNullOrEmpty()) return@collect
                var ratingList = mutableListOf<PetRating>()
                ratingList.addAll(uiState)
                var top = uiState.find { it.index == 1 }
                top?.let{
                    var petAdd = PetRating(-1, -1, "",
                        -1, "", -1, "",-1, "", -1,
                        "", "", "", -1, -1,
                        -1, "", "", -1, -1,
                        -1, listOf())
                    topRatingPet = it
                    if(uiState.size >= 4){
                        ratingList.add(3, petAdd)
                    }else if(uiState.size <= 3){
                        ratingList.add(2, petAdd)
                    }else if(uiState.size <= 2){
                        ratingList.add(1, petAdd)
                    }
                }
                listPet.addAll(ratingList)
                ratingPetAdapter.notifyDataSetChanged()
            }
        }

        binding!!.imageFilter.setOnClickListener {
            FilterPetsObject.show.value = 1
        }

        commitFilter()

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
                var new_list = it.toMutableList()
                new_list.add(0, UserPets())
                listPetUser.addAll(new_list)
                userPetAdapter.notifyDataSetChanged()
                ratingPetAdapter.addUserpets(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            ratingViewModel.uiStateUserPets.collect {
                listPet.clear()
                listPet.addAll(it)
                ratingPetAdapter.notifyDataSetChanged()
                scrollToPosition()
                loadTop = false
            }
        }

        lifecycleScope.launchWhenStarted {
            ratingViewModel.uiStateTopPets.collect {
                if(it.isNotEmpty()){
                    if(topRatingPet != null){
                        if(!listPet.contains(topRatingPet)) listPet.add(0, topRatingPet!!)
                    }
                    for(i in it.size -1 downTo 0){
                        listPet.add(1, it[i])
                        ratingPetAdapter.notifyItemInserted(1)
                    }
                    loadTop = false
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            ratingViewModel.uiLocation.collect {
                userLocation = it
                binding!!.tabs.isUserLocation = userLocation?.city_id != null
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
            var scroll = indexArray / 2 * cardHeight + 500
            binding?.listRating?.smoothScrollToPosition(indexArray +1)
//            val timer = object: CountDownTimer(2000, 2000) {
//                override fun onTick(millisUntilFinished: Long) {}
//                override fun onFinish() {
//
//                }
//            }
//            timer.start()

            Log.d(TAG, "click pet in position ${indexArray / 2}")
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
            if(listPet.size == 0) ratingViewModel.getRating(0, filterRaing)
            else ratingViewModel.getRating(listPet.last().index, filterRaing)
            loadMore = true
        }
    }

    private fun loadTop() {
        Log.d(TAG, "loadTop(**)")
        if(!loadTop){
            if(listPet.size == 0) return
            var i = listPet.find { it.index == 2 }
            i?.let {
                return@loadTop
            }
            if(listPet[1].index - 50 > 0){
                var off = listPet[1].index - 50
                ratingViewModel.getRatingToTop(off, null, filterRaing)
            }else {
                var lim = 50 - listPet[1].index
                ratingViewModel.getRatingToTop(1,lim, filterRaing)
            }
            loadTop = true
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
        var txt = when (UserInfo.getTabsFilter(requireContext())){
            2 -> getString(R.string.world) + " ,"
            1 -> "${userLocation?.country}, "
            0 -> "${userLocation?.city} ,"
            else -> getString(R.string.world) + " ,"
        }

        when (UserInfo.getTabsFilter(requireContext())){
            2 -> {
                filterRaing.cityId = null
                filterRaing.countryId = null
            }
            1 -> {
                filterRaing.countryId = userLocation?.country_id
            }
            0 -> {
                filterRaing.cityId = userLocation?.city_id
                filterRaing.countryId = userLocation?.country_id
            }
        }

        var itKinds = FilterPetsObject.listKinds.value
        if(itKinds.isEmpty()) {
            filterRaing.type = null
            txt += "${getString(R.string.all_kinds2).lowercase()}, "
        }
        else if(itKinds.size == 1) {
            filterRaing.type = itKinds[0].type
            txt += "${itKinds.get(0).name.lowercase()}, "
        }
        else {
            filterRaing.type = null
            txt += "${getString(R.string.other_kinds).lowercase()}, "
        }

        var itBreeds = FilterPetsObject.breed.value
        if (itBreeds.title.isNotEmpty()) {
            filterRaing.breedId = itBreeds.id
            txt += "${itBreeds.title.lowercase()}, "
        }else filterRaing.breedId = null

        if(itBreeds.id == -1) txt += "${getString(R.string.no_breeds).lowercase()}, "
        else if(itBreeds.id == 0) txt += "${getString(R.string.all_breeds).lowercase()}, "

        var tSex = when(FilterPetsObject.sex.value.type){
            0 -> getString(R.string.sex_all2)
            1 -> getString(R.string.sex_man)
            2 -> getString(R.string.sex_girl)
            else -> getString(R.string.sex_all2)
        }

        filterRaing.sex = when(FilterPetsObject.sex.value.type){
            0 -> null
            1 -> "MALE"
            2 -> "FEMALE"
            else -> null
        }

        txt += "${tSex.lowercase()}, "
        txt +=  "${FilterPetsObject.minValue.value} - ${FilterPetsObject.maxValue.value}"

        filterRaing.ageBetween =
            "${FilterPetsObject.minValue.value}:${FilterPetsObject.maxValue.value}"

        binding!!.filterText.text = txt

        listPet.clear()
        ratingViewModel.getRating(0, filterRaing)
    }

    override fun selected(tab: BesieTabSelected) {
        if(tab == BesieTabSelected.NullUserLocation) {
            UserLocationDialog().show(childFragmentManager, "")
            return
        }

        UserInfo.setTabsFilter(requireContext(),
            when(tab){
                BesieTabSelected.CITY -> 0
                BesieTabSelected.COUNTRY -> 1
                else -> 2
            })
        topRatingPet = null
        updateFilterText()
    }

    override fun onClick(pet: UserPets) {
        pet.id?.let {
            ratingViewModel.uiStateTopPets.value = listOf<PetRating>()
            ratingViewModel.getRatingMyPet(it, filterRaing)
            clickMyPet = it
            var index = listPetUser.indexOf(pet)
            userPetAdapter.selectPetPosition = index
            binding?.listPetsUser?.post {
                userPetAdapter.notifyDataSetChanged()
            }

        }
    }

    fun animBottomBar(shwo: Boolean){
//        val propertyXLeft: PropertyValuesHolder =
//            if(shwo) PropertyValuesHolder.ofFloat("PROPERTY_BOTTOM", 0f, 500f)
//            else PropertyValuesHolder.ofFloat("PROPERTY_BOTTOM", 500f, 0f)

        var lp = 72 * context?.resources?.displayMetrics?.density!!
        //var lp1 = 24 * context?.resources?.displayMetrics?.density!!
        val propertyYBottom: PropertyValuesHolder =
            if(shwo) PropertyValuesHolder.ofFloat("PROPERTY_BOTTOM_LP", lp.toFloat(),
                2f
            )
            else PropertyValuesHolder.ofFloat("PROPERTY_BOTTOM_LP",
                2f, lp.toFloat())

        animator!!.setValues( propertyYBottom)
        animator!!.setDuration(500)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            //var transitionY = animation.getAnimatedValue("PROPERTY_BOTTOM") as Float
            var lpY = animation.getAnimatedValue("PROPERTY_BOTTOM_LP") as Float
            //binding?.bottomBar?.translationY = transitionY
            var lpVB = binding?.bottomBar?.layoutParams
            lpVB?.height = lpY.toInt()
            binding?.bottomBar?.layoutParams = lpVB
        })

        animator!!.addListener(object : DynamicAnimation.OnAnimationEndListener,
            Animator.AnimatorListener {
            override fun onAnimationEnd(
                animation: DynamicAnimation<*>?,
                canceled: Boolean,
                value: Float,
                velocity: Float
            ) {
            }

            override fun onAnimationStart(p0: Animator?) {
                isAnimationbottomView = true
            }

            override fun onAnimationEnd(p0: Animator?) {
                isAnimationbottomView = false
                isShowbottomView = shwo
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

        })
        animator.start()
    }

    override fun onSearch() {

    }

    data class FilterRating(
        var sex: String? = null, // MALE, FEMALE
        var cityId: Int? = null,
        var countryId: Int? = null,
        var ageBetween: String = "0:200",
        var breedId: Int? = null,
        var type: String? = null
    )

}