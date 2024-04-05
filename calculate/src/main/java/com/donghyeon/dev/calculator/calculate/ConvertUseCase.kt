package com.donghyeon.dev.calculator.calculate

import javax.inject.Inject

enum class ConvertType {
    LENGTH,
    AREA,
    VOLUME,
    WEIGHT,
    SPEED,
    TIME,
    DATA,
}

val unitLengthList =
    listOf(
        "mm",
        "cm",
        "m",
        "km",
        "in",
        "ft",
        "yd",
        "mi",
    )

val unitAreaList =
    listOf(
        "cm²",
        "m²",
        "in²",
        "ft²",
        "ac",
        "a",
        "ha",
        "평(≈3.3m²)",
    )

val unitVolumeList =
    listOf(
        "ml",
        "l",
        "gal",
        "cm³",
        "m³",
        "in³",
        "ft³",
    )

val unitWeightList =
    listOf(
        "g",
        "kg",
        "t",
        "lb",
        "oz",
        "근(=600g)",
        "돈(=3.75g)",
    )

val unitTimeList =
    listOf(
        "ms",
        "s",
        "min",
        "h",
        "d",
    )

val unitSppedList =
    listOf(
        "m/s",
        "m/h",
        "km/s",
        "km/h",
        "in/s",
        "in/h",
        "ft/s",
        "ft/h",
        "mi/s",
        "mi/h",
    )

val unitDataList =
    listOf(
        "bit",
        "B",
        "KB",
        "MB",
        "GB",
        "TB",
    )

class ConvertUseCase
    @Inject
    constructor() {
        private val defaultValue: String = "?"

        operator fun invoke(
            type: ConvertType,
            value1: String,
            value2: String,
        ): String = "?"
    }
