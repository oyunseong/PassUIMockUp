package com.passuimockup

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.passuimockup.databinding.FragmentMobileIdBinding
import com.pxFromDp
import com.setDefaultPageTransformer
import com.showOtherPageEdges
import kotlinx.coroutines.launch
import kotlin.math.ceil

class MobileIDFragment : Fragment() {
    private var _binding: FragmentMobileIdBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var user:User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMobileIdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list: List<Int> = listOf(1, 2, 3)
        val tabTitleArray = arrayOf(
            "주민등록증",
            "운전면허증",
            "신분증 검증"
        )
        val list2: List<Int> = listOf(1, 2, 3, 4, 6)

//        val currentVisibleItemPx = 40.pxFromDp
//
//        binding.viewPager2.addItemDecoration(object : RecyclerView.ItemDecoration() {
//            override fun getItemOffsets(
//                outRect: Rect,
//                view: View,
//                parent: RecyclerView,
//                state: RecyclerView.State
//            ) {
//                outRect.right = currentVisibleItemPx.toInt()
//                outRect.left = currentVisibleItemPx.toInt()
//            }
//        })

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // 배너 무한스크롤
        binding.banner.adapter = BannerAdapter(list2)
        binding.banner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val bannerPosition = Int.MAX_VALUE / 2 - ceil(list2.size.toDouble() / 2).toInt()
        binding.banner.setCurrentItem(bannerPosition, false)


        if (viewModel.profile.value.isNotEmpty()) {

        }

        arguments?.let {
            val profileImage = it.getString("profileImage")
            val nickname = it.getString("nickname")
            val birthday = it.getString("birthday")
            val address = it.getString("address")
            val sex = it.getString("sex")
            val renewal = it.getString("renewal")
            val idNumber = it.getString("idNumber")
            val cardNumber = it.getString("cardNumber")
            val dateOfIssue = it.getString("DateOfIssue")
            val placeOfIssue = it.getString("placeOfIssue")

//            val inputStream =
//                requireContext().contentResolver.openInputStream(Uri.parse(profileImage))
//            val bitmap = BitmapFactory.decodeStream(inputStream)

            user = User(
                profileImage = profileImage ?: "",
                nickname = nickname ?: "",
                birthday = birthday ?: "",
                address = address ?: "",
                sex = sex ?: "",
                renewal = renewal ?: "",
                idNumber = idNumber ?: "",
                cardNumber = cardNumber ?: "",
                DateOfIssue = dateOfIssue ?: "",
                placeOfIssue = placeOfIssue ?: "",
            )
        }


        Log.d("++##", user.toString())

        binding.viewPager2.adapter = IdViewPagerAdapter(
            items = list,
            user = user
        )

        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}