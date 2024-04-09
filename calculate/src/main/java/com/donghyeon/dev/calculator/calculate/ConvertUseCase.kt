package com.donghyeon.dev.calculator.calculate

import javax.inject.Inject

enum class ConvertType {
    LENGTH,
    AREA,
    VOLUME,
    WEIGHT,
    SPEED,
    TIME,
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
        "평",
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
        "근",
        "돈",
    )

val unitTimeList =
    listOf(
        "ms",
        "s",
        "min",
        "h",
        "d",
        "w",
        "m",
        "y",
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

class ConvertUseCase
    @Inject
    constructor()
