package com.donghyeon.dev.calculator.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.donghyeon.dev.calculator.data.entity.GeneralHistory
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreService
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) {
        private val gson = Gson()

        private companion object DataStoreKey {
            val generalHistoryKey = stringPreferencesKey("GeneralHistory")
            val persentTypeKey = intPreferencesKey("PersentType")
            val persentValue1Key = stringPreferencesKey("PersentValue1")
            val persentValue2Key = stringPreferencesKey("PersentValue2")
        }

        val generalHistory: Flow<GeneralHistory> =
            dataStore.data.map { pref ->
                pref[generalHistoryKey]?.let {
                    try {
                        gson.fromJson(it, GeneralHistory::class.java)
                    } catch (e: Exception) {
                        GeneralHistory(emptyList())
                    }
                } ?: GeneralHistory(emptyList())
            }

        suspend fun saveGeneralHistory(history: GeneralHistory.History) {
            dataStore.edit { pref ->
                val historyList =
                    generalHistory.firstOrNull()?.historyList?.let {
                        it + listOf(history)
                    } ?: listOf(history)
                pref[generalHistoryKey] = gson.toJson(GeneralHistory(historyList))
            }
        }

        suspend fun clearGeneralHistory() {
            dataStore.edit { pref ->
                pref[generalHistoryKey] = ""
            }
        }

        val persentType: Flow<Int> =
            dataStore.data.map { it[persentTypeKey] ?: 0 }

        suspend fun savePersentType(type: Int) {
            dataStore.edit { it[persentTypeKey] = type }
        }
    }
