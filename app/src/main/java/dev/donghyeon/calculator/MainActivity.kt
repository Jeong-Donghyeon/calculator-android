package dev.donghyeon.calculator

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import dagger.hilt.android.AndroidEntryPoint
import dev.donghyeon.calculator.theme.Black

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            (LocalView.current.context as Activity).window.statusBarColor = Black.toArgb()
            MainScreen(viewModel = viewModel)
        }
    }
}
