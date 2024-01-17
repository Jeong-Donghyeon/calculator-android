package dev.donghyeon.calculator

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.donghyeon.calculator.common.LocalNavController
import dev.donghyeon.calculator.common.LocalViewModel
import dev.donghyeon.calculator.dialog.SheetMenu
import dev.donghyeon.calculator.general.GeneralScreen
import dev.donghyeon.calculator.info.InfoScreen
import dev.donghyeon.calculator.percent.PercentScreen
import dev.donghyeon.calculator.theme.ColorSet
import kotlinx.coroutines.flow.collectLatest

private var toast: Toast? = null

@Composable
fun MainScreen(viewModel: MainViewModel) {
    CompositionLocalProvider(
        LocalViewModel provides viewModel,
        LocalNavController provides rememberNavController(),
        content = {
            val context = LocalContext.current
            val navController = LocalNavController.current
            (LocalView.current.context as Activity).apply {
                window.statusBarColor = ColorSet.container.toArgb()
            }
            val menu by viewModel.menu.collectAsState()
            LaunchedEffect(Unit) {
                viewModel.nav.collectLatest {
                    when (it.second) {
                        Destination.Back -> navController.popBackStack()
                        Destination.Info -> navController.navigate(it.second.route)
                        Destination.General, Destination.Percent -> {
                            navController.navigate(it.second.route) {
                                launchSingleTop = true
                                popUpTo(it.first.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.toast.collectLatest {
                    toast?.cancel()
                    toast = Toast.makeText(context, it, Toast.LENGTH_SHORT)
                    toast?.show()
                }
            }
            Scaffold(containerColor = ColorSet.container) {
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = navController,
                    startDestination = Destination.START_SCREEN.route,
                ) {
                    composable(route = Destination.Info.route) { InfoScreen() }
                    composable(Destination.General.route) { GeneralScreen() }
                    composable(Destination.Percent.route) { PercentScreen() }
                }
            }
            if (menu) {
                SheetMenu(
                    close = { viewModel.closeMenu() },
                    nav = {
                        viewModel.closeMenu()
                        viewModel.nav(it)
                    },
                )
            }
        },
    )
}
