package dev.donghyeon.calculator

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.donghyeon.calculator.general.GeneralScreen
import dev.donghyeon.calculator.percent.PercentScreen
import dev.donghyeon.calculator.theme.ColorSet

@Composable
fun MainScreen(viewModel: MainViewModel) =
    CompositionLocalProvider(
        LocalViewModel provides viewModel,
        LocalNavController provides rememberNavController(),
        content = {
            val navController = LocalNavController.current
            (LocalView.current.context as Activity).apply {
                window.statusBarColor = ColorSet.container.toArgb()
            }
            Scaffold(containerColor = ColorSet.container) {
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = navController,
                    startDestination = "Percent",
                ) {
                    composable("General") { GeneralScreen() }
                    composable("Percent") { PercentScreen() }
                }
            }
        },
    )
