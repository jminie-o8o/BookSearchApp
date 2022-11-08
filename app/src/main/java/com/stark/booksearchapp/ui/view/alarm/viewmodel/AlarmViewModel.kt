package com.stark.booksearchapp.ui.view.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.booksearchapp.data.model.AlarmDisplayModel
import com.stark.booksearchapp.data.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
): ViewModel() {

    fun saveAlarm(model: AlarmDisplayModel) {
        viewModelScope.launch {
            alarmRepository.saveAlarm(model)
        }
    }

    suspend fun getAlarm() = withContext(Dispatchers.IO) {
        alarmRepository.getAlarm().first()
    }

    fun saveAlarmOnOff(model: AlarmDisplayModel) {
        viewModelScope.launch {
            alarmRepository.saveAlarmOnOff(model)
        }
    }

    suspend fun getAlarmOnOff() = withContext(Dispatchers.IO) {
        alarmRepository.getAlarmOnOff().first()
    }
}
