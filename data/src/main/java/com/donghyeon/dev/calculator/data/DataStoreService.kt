package com.donghyeon.dev.calculator.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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
        }

        val generalHistory: Flow<GeneralHistory?> =
            dataStore.data.map { pref ->
                pref[generalHistoryKey]?.let {
                    gson.fromJson(it, GeneralHistory::class.java)
                }
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
    }
