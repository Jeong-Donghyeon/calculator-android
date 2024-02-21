package com.donghyeon.dev.calculator.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.donghyeon.dev.calculator.data.entity.GeneralHistory
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
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

        suspend fun saveGeneralHistory(generalHistory: GeneralHistory) {
            dataStore.edit {
                it[generalHistoryKey] = gson.toJson(generalHistory)
            }
        }

        fun loadGeneralHistory(): Flow<GeneralHistory?> =
            dataStore.data.map { pref ->
                pref[generalHistoryKey]?.let {
                    gson.fromJson(it, GeneralHistory::class.java)
                }
            }
    }
