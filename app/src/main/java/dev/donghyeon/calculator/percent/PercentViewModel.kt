package dev.donghyeon.calculator.percent

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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
                        val index =
                            calculate.v2.selection.start.let {
                                if (it == 0) 0 else it - 1
                            }
                        calculate.copy(
                            v2 = calculate.v2.copy(selection = TextRange(index)),
                        )
                    } else {
                        val index =
                            calculate.v1.selection.start.let {
                                if (it == 0) 0 else it - 1
                            }
                        calculate.copy(
                            v1 = calculate.v1.copy(selection = TextRange(index)),
                        )
                    }
                NumberPadKey.RIGHT ->
                    if (calculate.select == ValueSelect.V2) {
                        val index = calculate.v2.selection.start + 1
                        calculate.copy(
                            v2 = calculate.v2.copy(selection = TextRange(index)),
                        )
                    } else {
                        val index = calculate.v1.selection.start + 1
                        calculate.copy(
                            v1 = calculate.v1.copy(selection = TextRange(index)),
                        )
                    }
                else -> {
                    val inputKey: (key: NumberPadKey, value: TextFieldValue) -> String = { k, v ->
                        StringBuilder(v.text).let {
                            val index = v.selection.start
                            when (k) {
                                NumberPadKey.BACK -> {
                                    if (index == 0) {
                                        it.toString()
                                    } else {
                                        it.delete(index - 1, index).toString()
                                    }
                                }
                                else -> it.insert(index, k.value).toString()
                            }
                        }
                    }

                    when (calculate.select) {
                        ValueSelect.V2 -> {
                            val inputTxt = inputKey(key, calculate.v2)
                            val index =
                                calculate.v2.selection.start.let {
                                    if (key == NumberPadKey.BACK) {
                                        if (it == 0) 0 else it - 1
                                    } else {
                                        it + 1
                                    }
                                }
                            calculate.copy(
                                v2 =
                                    calculate.v2.copy(
                                        text = inputTxt,
                                        selection = TextRange(index),
                                    ),
                            )
                        }
                        else -> {
                            val inputTxt = inputKey(key, calculate.v1)
                            val index =
                                calculate.v1.selection.start.let {
                                    if (key == NumberPadKey.BACK) {
                                        if (it == 0) 0 else it - 1
                                    } else {
                                        it + 1
                                    }
                                }
                            calculate.copy(
                                v1 =
                                    calculate.v1.copy(
                                        text = inputTxt,
                                        selection = TextRange(index),
                                    ),
                            )
                        }
                    }.let {
                        it.copy(
                            result =
                                when (state.value.select) {
                                    PercentSelect.CALCULATE2 -> percentCalculate2UseCase(it.v1.text, it.v2.text)
                                    PercentSelect.CALCULATE3 -> percentCalculate3UseCase(it.v1.text, it.v2.text)
                                    PercentSelect.CALCULATE4 -> percentCalculate4UseCase(it.v1.text, it.v2.text)
                                    else -> percentCalculate1UseCase(it.v1.text, it.v2.text)
                                },
                        )
                    }
                }
            }
    }
