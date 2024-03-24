package com.donghyeon.dev.calculator.data

import com.donghyeon.dev.calculator.data.entity.GeneralHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository
    @Inject
    constructor(
        private val dispatcher: Dispatcher,
        private val dataStoreService: DataStoreService,
    ) {
        val generalHistory: Flow<GeneralHistory> =
            dataStoreService.generalHistory.flowOn(dispatcher.io)

        suspend fun saveGeneralHistory(history: GeneralHistory.History) =
            withContext(dispatcher.io) {
                dataStoreService.saveGeneralHistory(history)
            }

        suspend fun clearGeneralHistory() =
            withContext(dispatcher.io) {
                dataStoreService.clearGeneralHistory()
            }

        suspend fun savePersentType(type: Int) =
            withContext(dispatcher.io) {
                dataStoreService.savePersentType(type)
            }

        suspend fun loadPersentType() = dataStoreService.persentType.flowOn(dispatcher.io).first()
    }
