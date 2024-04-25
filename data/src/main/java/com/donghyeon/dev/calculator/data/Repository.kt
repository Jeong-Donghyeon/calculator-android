package com.donghyeon.dev.calculator.data

import com.donghyeon.dev.calculator.data.entity.GeneralHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
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

    suspend fun savePercentType(type: Int) =
        withContext(dispatcher.io) {
            dataStoreService.savePercentType(type)
        }

    suspend fun loadPercentType() = dataStoreService.percentType.flowOn(dispatcher.io).first()

    suspend fun saveRatioType(type: Int) =
        withContext(dispatcher.io) {
            dataStoreService.saveRatioType(type)
        }

    suspend fun loadRatioType() = dataStoreService.ratioType.flowOn(dispatcher.io).first()

    suspend fun saveConvertType(type: Int) =
        withContext(dispatcher.io) {
            dataStoreService.saveConvertType(type)
        }

    suspend fun loadConvertType() = dataStoreService.convertType.flowOn(dispatcher.io).first()

    suspend fun saveDateType(type: Int) =
        withContext(dispatcher.io) {
            dataStoreService.saveDateType(type)
        }

    suspend fun loadDateType() = dataStoreService.dateType.flowOn(dispatcher.io).first()
}
