package com.donghyeon.dev.calculator

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.donghyeon.dev.calculator.common.LocalNavController
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.StartSceen
import com.donghyeon.dev.calculator.general.GeneralScreen
import com.donghyeon.dev.calculator.info.InfoScreen
import com.donghyeon.dev.calculator.percent.PercentScreen
import com.donghyeon.dev.calculator.theme.ColorSet
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
                    MainBottomMenu(
                        destination = bottomMenu.second,
                        nav = { viewModel.navigation(it) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview_MainBottomMenu() {
    MainBottomMenu(destination = Destination.GENERAL)
}

@Composable
private fun MainBottomMenu(
    destination: Destination,
    nav: ((Navigation) -> Unit)? = null,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 5.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        listOf(
            Destination.GENERAL to 26.dp,
            Destination.PERCENT to 24.dp,
            Destination.RATIO to 24.dp,
            Destination.CONVERT to 26.dp,
            Destination.CURRENCY to 24.dp,
            Destination.DATE to 24.dp,
        ).forEach {
            Button(
                modifier = Modifier.weight(1f).height(40.dp),
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = ColorSet.button,
                    ),
                elevation = null,
                onClick = {
                    nav?.invoke(Navigation.PopPush(it.first))
                },
            ) {
                Icon(
                    modifier =
                        Modifier
                            .padding(top = 1.dp)
                            .size(it.second),
                    painter = painterResource(id = it.first.icon),
                    tint =
                        if (it.first == destination) {
                            ColorSet.select
                        } else {
                            ColorSet.text
                        },
                    contentDescription = "KeyIcon",
                )
            }
        }
    }
}
