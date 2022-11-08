package com.stark.booksearchapp.data.repository

import com.stark.booksearchapp.data.model.AlarmDisplayModel
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun saveAlarm(model: AlarmDisplayModel)

    suspend fun getAlarm(): Flow<String>

    suspend fun saveAlarmOnOff(model: AlarmDisplayModel)

    suspend fun getAlarmOnOff(): Flow<Boolean>
}
