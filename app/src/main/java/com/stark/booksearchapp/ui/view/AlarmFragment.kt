package com.stark.booksearchapp.ui.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stark.booksearchapp.R
import com.stark.booksearchapp.data.model.AlarmDisplayModel
import com.stark.booksearchapp.databinding.FragmentAlarmBinding
import com.stark.booksearchapp.ui.viewmodel.AlarmViewModel
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

        // 뷰를 초기화 해주기
        initOnOffButton()
        initChangeAlarmTimeButton()
        lifecycleScope.launch {
            // 저장된 데이터 가져오기
            val model = fetchDataFromSharedPreferences()
            // 뷰에 데이터를 그려주기
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
        // DB 에서 데이터 가져오기
        val alarmModel = lifecycleScope.async {
            // DataStore 에서 데이터 가져오기
            val timeDBValue = alarmViewModel.getAlarm()
            val onOffDBValue = alarmViewModel.getAlarmOnOff()

            // 시:분 형식으로 가져온 데이터 스플릿
            val alarmData = timeDBValue.split(":")
            val alarmModel =
                AlarmDisplayModel(alarmData[0].toInt(), alarmData[1].toInt(), onOffDBValue)

            // 보정 조정 예외처 (브로드 캐스트 가져오기)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                M_ALARM_REQUEST_CODE,
                Intent(requireContext(), AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            ) // 있으면 가져오고 없으면 안만든다. (null)

            if ((pendingIntent == null) and alarmModel.onOff) {
                //알람은 꺼져있는데, 데이터는 켜져있는 경우
                alarmModel.onOff = false

            } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
                // 알람은 켜져있는데 데이터는 꺼져있는 경우.
                // 알람을 취소함
                pendingIntent.cancel()
            }
            alarmModel
        }
        return alarmModel.await()
    }

    // 알람 켜기 끄기 버튼.
    private fun initOnOffButton() {
        val onOffButton = binding.onOffButton
        onOffButton.setOnClickListener {
            // 저장한 데이터를 확인한다
            val model =
                it.tag as? AlarmDisplayModel ?: return@setOnClickListener// 형변환 실패하는 경우에는 null
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not()) // on off 스위칭
            renderView(newModel)

            // 온/오프 에 따라 작업을 처리한다
            if (newModel.onOff) {
                // 온 -> 알람을 등록
                val calender = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)
                    // 지나간 시간의 경우 다음날 알람으로 울리도록
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1) // 하루 더하기
                    }
                }

                //알람 매니저 가져오기.
                val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    M_ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                ) // 있으면 새로 만든거로 업데이트

                alarmManager.setInexactRepeating( // 정시에 반복
                    AlarmManager.RTC_WAKEUP, // RTC_WAKEUP : 실제 시간 기준으로 wakeup , ELAPSED_REALTIME_WAKEUP : 부팅 시간 기준으로 wakeup
                    calender.timeInMillis, // 언제 알람이 발동할지.
                    AlarmManager.INTERVAL_DAY, // 하루에 한번씩.
                    pendingIntent
                )
            } else {
                // 오프 -> 알람을 제거
                cancelAlarm()
            }
        }
    }

    // 시간 재설정 버튼.
    private fun initChangeAlarmTimeButton() {
        val changeAlarmButton = binding.changeAlarmTimeButton
        changeAlarmButton.setOnClickListener {
            // 현재 시간을 가져오기 위해 캘린더 인스턴스 생성
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                // TimePickDialog 띄워줘서 시간을 설정을 하게끔 하고, 그 시간을 가져와서
                // 데이터를 저장
                val model = saveAlarmModel(hour, minute, false)
                // 뷰를 업데이트
                renderView(model)
                // 기존에 있던 알람을 삭제한다.
                cancelAlarm()
            }
            val dialog = TimePickerDialog(requireContext(),
                android.R.style.ThemeOverlay_Material_Dialog_Alert,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun cancelAlarm() {
        // 기존에 있던 알람을 삭제한다.
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            M_ALARM_REQUEST_CODE,
            Intent(requireContext(), AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) // 있으면 가져오고 없으면 안만든다. (null)

        pendingIntent?.cancel() // 기존 알람 삭제
    }

    private fun renderView(model: AlarmDisplayModel) {
        // 최초 실행 또는 시간 재설정 시 들어옴
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
        // static 영역 (상수 지정)
        private val M_ALARM_REQUEST_CODE = 1000
    }
}
