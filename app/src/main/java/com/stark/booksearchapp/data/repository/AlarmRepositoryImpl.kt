package com.stark.booksearchapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.stark.booksearchapp.data.model.AlarmDisplayModel
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.stark.booksearchapp.data.repository.AlarmRepositoryImpl.PreferencesKeys.ALARM_ON_OFF
import com.stark.booksearchapp.data.repository.AlarmRepositoryImpl.PreferencesKeys.ALARM_TIME
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AlarmRepository {
    // DataStore
    private object PreferencesKeys {
        val ALARM_TIME =
            stringPreferencesKey("alarm_time")
        val ALARM_ON_OFF =
            booleanPreferencesKey("alarm_on_off")
    }

    override suspend fun saveAlarm(model: AlarmDisplayModel) {
        dataStore.edit { prefs ->
            prefs[ALARM_TIME] = model.makeDataForDB()
        }
    }

    override suspend fun getAlarm(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[ALARM_TIME] ?: "00:00"
            }
    }

    override suspend fun saveAlarmOnOff(model: AlarmDisplayModel) {
        dataStore.edit { prefs ->
            prefs[ALARM_ON_OFF] = model.onOff
        }
    }

    override suspend fun getAlarmOnOff(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[ALARM_ON_OFF] ?: false
            }
    }
}
