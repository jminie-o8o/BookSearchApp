package com.stark.booksearchapp.ui.view.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stark.booksearchapp.R
import com.stark.booksearchapp.data.model.AlarmDisplayModel
import com.stark.booksearchapp.databinding.FragmentAlarmBinding
import com.stark.booksearchapp.ui.view.MainActivity
import com.stark.booksearchapp.ui.view.alarm.viewmodel.AlarmViewModel
import com.stark.booksearchapp.util.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private val alarmViewModel: AlarmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnOffButton()
        initChangeAlarmTimeButton()
        lifecycleScope.launch {
            val model = fetchDataFromSharedPreferences()
            renderView(model)
        }
        hideBottomNavigation()
        goBack()
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        alarmViewModel.saveAlarm(model)
        alarmViewModel.saveAlarmOnOff(model)
        return model
    }

    private suspend fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val alarmModel = lifecycleScope.async {
            val timeDBValue = alarmViewModel.getAlarm()
            val onOffDBValue = alarmViewModel.getAlarmOnOff()
            val alarmData = timeDBValue.split(":")
            val alarmModel =
                AlarmDisplayModel(alarmData[0].toInt(), alarmData[1].toInt(), onOffDBValue)

            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                M_ALARM_REQUEST_CODE,
                Intent(requireContext(), AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            if ((pendingIntent == null) and alarmModel.onOff) {
                alarmModel.onOff = false

            } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
                pendingIntent.cancel()
            }
            alarmModel
        }
        return alarmModel.await()
    }

    private fun initOnOffButton() {
        val onOffButton = binding.onOffButton
        onOffButton.setOnClickListener {
            val model =
                it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())
            renderView(newModel)

            if (newModel.onOff) {
                val calender = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }
                val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    M_ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calender.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            } else {
                cancelAlarm()
            }
        }
    }

    private fun initChangeAlarmTimeButton() {
        val changeAlarmButton = binding.changeAlarmTimeButton
        changeAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                val model = saveAlarmModel(hour, minute, false)
                renderView(model)
                cancelAlarm()
            }
            val dialog = TimePickerDialog(
                requireContext(),
                android.R.style.ThemeOverlay_Material_Dialog_Alert,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            M_ALARM_REQUEST_CODE,
            Intent(requireContext(), AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        pendingIntent?.cancel()
    }

    private fun renderView(model: AlarmDisplayModel) {
        binding.ampmTextView.apply {
            text = model.ampmText
        }
        binding.timeTextView.apply {
            text = model.timeText
        }
        binding.onOffButton.apply {
            text = model.onOffText
            tag = model
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation =
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigation.visibility = View.GONE
    }

    private fun goBack() {
        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private val M_ALARM_REQUEST_CODE = 1000
    }
}
