package dev.donghyeon.calculator.percent

import androidx.compose.ui.text.TextRange
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.donghyeon.calculator.BaseViewModel
import dev.donghyeon.calculator.domain.PercentCalculate1UseCase
import dev.donghyeon.calculator.domain.PercentCalculate2UseCase
import dev.donghyeon.calculator.domain.PercentCalculate3UseCase
import dev.donghyeon.calculator.domain.PercentCalculate4UseCase
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
            when (key) {
                NumberPadKey.CLEAR -> PercentData.Calculate()
                NumberPadKey.LEFT ->
                    if (calculate.select == ValueSelect.V2) {
                        calculate.copy(
                            value2 = calculate.value2,
                            v2 =
                                calculate.v2.copy(
                                    text = calculate.value2,
                                    selection =
                                        TextRange(
                                            start = calculate.v2.selection.start - 1,
                                            end = calculate.v2.selection.end - 1,
                                        ),
                                ),
                        )
                    } else {
                        calculate.copy(
                            value1 = calculate.value1,
                            v1 =
                                calculate.v1.copy(
                                    text = calculate.value1,
                                    selection =
                                        TextRange(
                                            start = calculate.v1.selection.start - 1,
                                            end = calculate.v1.selection.end - 1,
                                        ),
                                ),
                        )
                    }
                NumberPadKey.RIGHT ->
                    if (calculate.select == ValueSelect.V2) {
                        calculate.copy(
                            value2 = calculate.value2,
                            v2 =
                                calculate.v2.copy(
                                    text = calculate.value2,
                                    selection =
                                        TextRange(
                                            start = calculate.v2.selection.start + 1,
                                            end = calculate.v2.selection.end + 1,
                                        ),
                                ),
                        )
                    } else {
                        calculate.copy(
                            value1 = calculate.value1,
                            v1 =
                                calculate.v1.copy(
                                    text = calculate.value1,
                                    selection =
                                        TextRange(
                                            start = calculate.v1.selection.start + 1,
                                            end = calculate.v1.selection.end + 1,
                                        ),
                                ),
                        )
                    }
                else -> {
                    val inputKey: (key: NumberPadKey, value: String) -> String = { k, v ->
                        when (k) {
                            NumberPadKey.BACK -> v.dropLast(1)
                            else -> v + k.value
                        }
                    }
                    when (calculate.select) {
                        ValueSelect.V2 -> {
                            val inputTxt = inputKey(key, calculate.value2)
                            calculate.copy(
                                value2 = inputTxt,
                                v2 =
                                    calculate.v2.copy(
                                        text = inputTxt,
                                        selection =
                                            TextRange(
                                                start = calculate.v2.selection.start + 1,
                                                end = calculate.v2.selection.end + 1,
                                            ),
                                    ),
                            )
                        }
                        else -> {
                            val inputTxt = inputKey(key, calculate.value1)
                            calculate.copy(
                                value1 = inputTxt,
                                v1 =
                                    calculate.v1.copy(
                                        text = inputTxt,
                                        selection =
                                            TextRange(
                                                start = calculate.v1.selection.start + 1,
                                                end = calculate.v1.selection.end + 1,
                                            ),
                                    ),
                            )
                        }
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
    }
