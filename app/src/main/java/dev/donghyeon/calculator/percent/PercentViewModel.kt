package dev.donghyeon.calculator.percent

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.donghyeon.calculator.BaseViewModel
import dev.donghyeon.calculator.usecase.percent.PercentCalculate1UseCase
import dev.donghyeon.calculator.usecase.percent.PercentCalculate2UseCase
import dev.donghyeon.calculator.usecase.percent.PercentCalculate3UseCase
import dev.donghyeon.calculator.usecase.percent.PercentCalculate4UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface PercentAction {
    fun inputPercentSelect(select: PercentSelect)

    fun inputValueSelect(select: ValueSelect)

    fun inputNumberKeyPad(key: NumberPadKey)
}

@HiltViewModel
class PercentViewModel
    @Inject
    constructor(
        private val percentCalculate1UseCase: PercentCalculate1UseCase,
        private val percentCalculate2UseCase: PercentCalculate2UseCase,
        private val percentCalculate3UseCase: PercentCalculate3UseCase,
        private val percentCalculate4UseCase: PercentCalculate4UseCase,
    ) : BaseViewModel(), PercentAction {
        private val _state = MutableStateFlow(PercentData())
        val state: StateFlow<PercentData> = _state

        override fun inputPercentSelect(select: PercentSelect) {
            _state.value = state.value.copy(select = select)
        }

        override fun inputValueSelect(select: ValueSelect) {
            _state.value =
                state.value.let {
                    when (it.select) {
                        PercentSelect.CALCULATE2 -> it.copy(calculate2 = it.calculate2.copy(select = select))
                        PercentSelect.CALCULATE3 -> it.copy(calculate3 = it.calculate3.copy(select = select))
                        PercentSelect.CALCULATE4 -> it.copy(calculate4 = it.calculate4.copy(select = select))
                        else -> it.copy(calculate1 = it.calculate1.copy(select = select))
                    }
                }
        }

        override fun inputNumberKeyPad(key: NumberPadKey) {
            _state.value =
                state.value.let {
                    when (it.select) {
                        PercentSelect.CALCULATE2 ->
                            it.copy(
                                calculate2 =
                                    input(
                                        key = key,
                                        calculate = it.calculate2,
                                    ),
                            )
                        PercentSelect.CALCULATE3 ->
                            it.copy(
                                calculate3 =
                                    input(
                                        key = key,
                                        calculate = it.calculate3,
                                    ),
                            )

                        PercentSelect.CALCULATE4 ->
                            it.copy(
                                calculate4 =
                                    input(
                                        key = key,
                                        calculate = it.calculate4,
                                    ),
                            )

                        else -> it.copy(calculate1 = input(key = key, calculate = it.calculate1))
                    }
                }
        }

        private fun input(
            key: NumberPadKey,
            calculate: PercentData.Calculate,
        ): PercentData.Calculate =
            if (key == NumberPadKey.CLEAR) {
                PercentData.Calculate()
            } else {
                val inputKey: (key: NumberPadKey, value: String) -> String = { k, v ->
                    when (k) {
                        NumberPadKey.BACK -> v.dropLast(1)
                        else -> v + k.value
                    }
                }
                when (calculate.select) {
                    ValueSelect.V2 -> calculate.copy(value2 = inputKey(key, calculate.value2))
                    else -> calculate.copy(value1 = inputKey(key, calculate.value1))
                }.let {
                    it.copy(
                        result =
                            when (state.value.select) {
                                PercentSelect.CALCULATE2 -> percentCalculate2UseCase(it.value1, it.value2)
                                PercentSelect.CALCULATE3 -> percentCalculate3UseCase(it.value1, it.value2)
                                PercentSelect.CALCULATE4 -> percentCalculate4UseCase(it.value1, it.value2)
                                else -> percentCalculate1UseCase(it.value1, it.value2)
                            },
                    )
                }
            }
    }
