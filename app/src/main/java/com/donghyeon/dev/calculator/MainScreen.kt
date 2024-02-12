package com.donghyeon.dev.calculator

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.donghyeon.dev.calculator.common.LocalNavController
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.StartSceen
import com.donghyeon.dev.calculator.dialog.SheetMenu
import com.donghyeon.dev.calculator.general.GeneralScreen
import com.donghyeon.dev.calculator.info.InfoScreen
import com.donghyeon.dev.calculator.percent.PercentScreen
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.view.ViewBottomMenu
import kotlinx.coroutines.flow.collectLatest

private var toast: Toast? = null

@Composable
fun MainScreen(viewModel: MainViewModel) {
    CompositionLocalProvider(
        LocalViewModel provides viewModel,
        LocalNavController provides rememberNavController(),
    ) {
        val context = LocalContext.current
        val navController = LocalNavController.current
        (LocalView.current.context as Activity).apply {
            window.statusBarColor = ColorSet.background.toArgb()
        }
        val menuState by viewModel.menuState.collectAsState()
        val bottomMenu by viewModel.bottomMunu.collectAsState()
        LaunchedEffect(Unit) {
            viewModel.navigationFlow.collectLatest {
                val (exit, current, next) = it
                if (exit) {
                    (context as Activity).finish()
                } else {
                    when (next) {
                        is Navigation.Pop -> {
                            navController.popBackStack()
                        }
                        is Navigation.PopToDestination -> {
                            navController.popBackStack(
                                route = next.destination.route,
                                inclusive = false,
                            )
                        }
                        is Navigation.Push -> {
                            navController.navigate(next.destination.route)
                        }
                        is Navigation.PopPush -> {
                            navController.navigate(next.destination.route) {
                                popUpTo(current.route) { inclusive = true }
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
        Scaffold(containerColor = ColorSet.background) {
            Column {
                NavHost(
                    modifier = Modifier.padding(it).weight(1f),
                    navController = navController,
                    startDestination = StartSceen.route,
                    enterTransition = { fadeIn(animationSpec = tween(0)) },
                    exitTransition = { fadeOut(animationSpec = tween(0)) },
                ) {
                    val pushEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            tween(100),
                        )
                    }
                    val pushPopExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            tween(100),
                        )
                    }
                    composable(
                        route = Destination.INFO.route,
                        enterTransition = pushEnterTransition,
                        popExitTransition = pushPopExitTransition,
                    ) { InfoScreen() }
                    composable(Destination.GENERAL.route) { GeneralScreen() }
                    composable(Destination.PERCENT.route) { PercentScreen() }
                }
                if (bottomMenu.first) {
                    ViewBottomMenu(
                        destination = bottomMenu.second,
                        menu = { viewModel.openMenu() },
                        nav = { viewModel.navigation(it) },
                    )
                }
            }
        }
        if (menuState) {
            SheetMenu(
                close = { viewModel.closeMenu() },
                nav = {
                    viewModel.closeMenu()
                    viewModel.navigation(Navigation.PopPush(it))
                },
            )
        }
    }
}
