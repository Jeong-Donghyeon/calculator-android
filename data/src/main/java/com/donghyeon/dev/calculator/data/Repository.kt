package com.donghyeon.dev.calculator.data

import com.donghyeon.dev.calculator.data.entity.GeneralHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository
    @Inject
    constructor(
        private val dispatcher: Dispatcher,
        private val dataStoreService: DataStoreService,
    ) {
        suspend fun saveGeneralHistory(history: GeneralHistory) =
            withContext(dispatcher.io) {
                dataStoreService.saveGeneralHistory(history)
            }

        fun loadGeneralHistory(): Flow<GeneralHistory?> = dataStoreService.loadGeneralHistory()
    }
