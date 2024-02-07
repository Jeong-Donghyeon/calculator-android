package com.donghyeon.dev.calculator.common

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.donghyeon.dev.calculator.MainViewModel

val LocalViewModel =
    staticCompositionLocalOf<MainViewModel> {
        error("No MainViewModel provided")
    }

val LocalNavController =
    staticCompositionLocalOf<NavHostController> {
        error("No NavHostController provided")
    }
