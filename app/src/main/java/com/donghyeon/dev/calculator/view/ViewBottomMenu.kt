package com.donghyeon.dev.calculator.view

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donghyeon.dev.calculator.Destination
import com.donghyeon.dev.calculator.Navigation
import com.donghyeon.dev.calculator.theme.ColorSet

@Preview
@Composable
fun Preview_ViewBottomMenu() {
    ViewBottomMenu(destination = Destination.GENERAL)
}

@Composable
fun ViewBottomMenu(
    destination: Destination,
    menu: (() -> Unit)? = null,
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
            Destination.RATIO to 24.dp,
            Destination.PERCENT to 24.dp,
            Destination.GENERAL to 26.dp,
            Destination.MENU to 24.dp,
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
                    if (it.first == Destination.MENU) {
                        menu?.invoke()
                    } else {
                        nav?.invoke(Navigation.PopPush(it.first))
                    }
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
