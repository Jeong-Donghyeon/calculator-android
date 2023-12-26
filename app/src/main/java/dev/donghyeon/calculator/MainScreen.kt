package dev.donghyeon.calculator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.donghyeon.calculator.general.GeneralScreen
import dev.donghyeon.calculator.percent.PercentScreen

@Composable
fun MainScreen(viewModel: MainViewModel) = CompositionLocalProvider(
    LocalViewModel provides viewModel,
    LocalNavController provides rememberNavController(),
    content = {
        NavHost(
            navController = LocalNavController.current,
            startDestination = "Percent"
        ) {
            composable("General") {
                GeneralScreen()
            }
            composable("Percent") {
                PercentScreen()
            }
        }
    }
)
