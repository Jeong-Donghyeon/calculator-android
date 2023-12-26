package dev.donghyeon.calculator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(viewModel: MainViewModel) = CompositionLocalProvider(
    LocalViewModel provides viewModel,
    LocalNavController provides rememberNavController(),
    content = {
        NavHost(
            navController = LocalNavController.current,
            startDestination = "General"
        ) {
            composable("General") {
                GeneralScreen()
            }
        }
    }
)
