package com.stark.booksearchapp.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stark.booksearchapp.R
import com.stark.booksearchapp.databinding.FragmentSettingBinding
import com.stark.booksearchapp.ui.viewmodel.SettingsViewModel
import com.stark.booksearchapp.util.Sort
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveSettings()
        loadSettings()
        showWorkStatus()
        goToAlarmFragment()
        showBottomNavigation()
    }

    private fun saveSettings() {
        binding.rgSort.setOnCheckedChangeListener { _, checkedId ->
            val value = when (checkedId) {
                R.id.rb_accuracy -> Sort.ACCURACY.value
                R.id.rb_latest -> Sort.LATEST.value
                else -> return@setOnCheckedChangeListener
            }
            settingViewModel.saveSortMode(value)
        }

        binding.swCacheDelete.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveCacheDeleteMode(isChecked)
            if (isChecked) {
                settingViewModel.setWork()
            } else {
                settingViewModel.deleteWork()
            }
        }
    }

    private fun loadSettings() {
        lifecycleScope.launch {
            val buttonId = when (settingViewModel.getSortMode()) {
                Sort.ACCURACY.value -> R.id.rb_accuracy
                Sort.LATEST.value -> R.id.rb_latest
                else -> return@launch
            }
            binding.rgSort.check(buttonId)
        }

        // WorkManger
        lifecycleScope.launch {
            val mode = settingViewModel.getCacheDeleteMode()
            binding.swCacheDelete.isChecked = mode
        }
    }

    // LiveData 로 반환받은 작업상태를 반환
    private fun showWorkStatus() {
        settingViewModel.getWorkStatus().observe(viewLifecycleOwner) { workInfo ->
            Log.d("WorkManger", workInfo.toString())
            if (workInfo.isEmpty()) {
                binding.tvWorkStatus.text = "비활성화"
            } else {
                binding.tvWorkStatus.text = when(workInfo[0].state.toString()) {
                    "ENQUEUED" -> "활성화"
                    else -> "취소"
                }
            }
        }
    }

    private fun goToAlarmFragment() {
        binding.btnSettingAlarm.setOnClickListener {
            val action = SettingFragmentDirections.actionFragmentSettingToAlarmFragment()
            findNavController().navigate(action)
        }
    }

    private fun showBottomNavigation() {
        val bottomNavigation = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
