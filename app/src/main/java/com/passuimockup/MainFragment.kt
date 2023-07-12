package com.passuimockup

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dataStore
import com.passuimockup.databinding.FragmentMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val REQUEST_IMAGE_PICK = 1
    val IMAGE_KEY = stringPreferencesKey("image")
    val NICKNAME_KEY = stringPreferencesKey("nickname")
    val CARD_NUMBER_KEY = stringPreferencesKey("cardNumber")
    val ADDRESS_KEY = stringPreferencesKey("address")
    val BIRTHDAY_KEY = stringPreferencesKey("birthday")
    val RENEWAL_KEY = stringPreferencesKey("renewal")
    val ID_NUMBER_KEY = stringPreferencesKey("idNumber")
    val PLACE_KEY = stringPreferencesKey("placeOfIssue")
    val DATE_KEY = stringPreferencesKey("dateOfIssue")
    val SEX_KEY = stringPreferencesKey("sex")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_IMAGE_PICK
            )
        }

        lifecycleScope.launch {
            viewModel.successLoadData.collect {
                binding.dateOfIssue.setText(viewModel.dateOfIssue.value)
                binding.cardNumber.setText(viewModel.cardNumber.value)
                binding.nickName.setText(viewModel.nickname.value)
                binding.sex.setText(viewModel.sex.value)
                binding.renewal.setText(viewModel.renewal.value)
                binding.address.setText(viewModel.address.value)
                binding.birthday.setText(viewModel.birthday.value)
                binding.idNumber.setText(viewModel.idNumber.value)
            }
        }


        binding.confirmButton.setOnClickListener {
            if (binding.dateOfIssue.text.isEmpty() || viewModel.profile.value == "") {
                Toast.makeText(requireContext(), "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 확인버튼 눌렀을 때,
                // edittext에 있는 값을 보고 뷰모델에 저장하고, 뷰모델에 저장된 값을 datastore에 저장
                lifecycleScope.launch {
                    viewModel.saveData(NICKNAME_KEY, binding.nickName.text.toString())
                    viewModel.saveData(BIRTHDAY_KEY, binding.birthday.text.toString())
                    viewModel.saveData(ADDRESS_KEY, binding.address.text.toString())
                    viewModel.saveData(CARD_NUMBER_KEY, binding.cardNumber.text.toString())
                    viewModel.saveData(RENEWAL_KEY, binding.renewal.text.toString())
                    viewModel.saveData(ID_NUMBER_KEY, binding.idNumber.text.toString())
                    viewModel.saveData(PLACE_KEY, binding.placeOfIssue.text.toString())
                    viewModel.saveData(DATE_KEY, binding.dateOfIssue.text.toString())
                    viewModel.saveData(SEX_KEY, binding.sex.text.toString())
                    viewModel.saveData(IMAGE_KEY, viewModel.profile.value)

                    val bundle = Bundle().apply {

                        putString("profileImage", viewModel.profile.value)
                        putString("nickname", binding.nickName.text.toString())
                        putString("birthday", binding.birthday.text.toString())
                        putString("address", binding.address.text.toString())
                        putString("sex", binding.sex.text.toString())
                        putString("renewal", binding.renewal.text.toString())
                        putString("idNumber", binding.idNumber.text.toString())
                        putString("cardNumber", binding.cardNumber.text.toString())
                        putString("DateOfIssue", binding.dateOfIssue.text.toString())
                        putString("placeOfIssue", binding.placeOfIssue.text.toString())
                    }

                    findNavController().navigate(
                        R.id.action_mainFragment_to_mobileIDFragment,
                        bundle
                    )
                }
            }

        }

        binding.profileImageUploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            val imagePath: String? = selectedImageUri?.toString()

            // 이미지 경로를 DataStore에 저장
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.saveData(
                    key = IMAGE_KEY,
                    value = imagePath ?: ""
                )
                viewModel.loadData(IMAGE_KEY, Type.IMAGE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


