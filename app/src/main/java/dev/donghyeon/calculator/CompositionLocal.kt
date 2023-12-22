package dev.donghyeon.calculator

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalViewModel = staticCompositionLocalOf<MainViewModel> {
    error("No MainViewModel provided")
}

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No navigation host controller provided")
}
