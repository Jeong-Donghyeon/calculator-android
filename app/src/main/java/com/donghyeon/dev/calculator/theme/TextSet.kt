package com.donghyeon.dev.calculator.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.R

private val fontFamily: FontFamily =
    FontFamily(
        Font(R.font.pretendard_thin, FontWeight.Thin),
        Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
        Font(R.font.pretendard_light, FontWeight.Light),
        Font(R.font.pretendard_regular, FontWeight.Normal),
        Font(R.font.pretendard_medium, FontWeight.Medium),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold),
        Font(R.font.pretendard_bold, FontWeight.Bold),
        Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
        Font(R.font.pretendard_black, FontWeight.Black),
    )

object TextSet {
    val thin: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Thin,
            lineHeight = 16.sp,
        )
    val extraLight: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.ExtraLight,
            lineHeight = 16.sp,
        )
    val light: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Light,
            lineHeight = 16.sp,
        )
    val regular: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.sp,
        )
    val medium: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp,
        )
    val semiBold: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp,
        )
    val bold: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp,
        )
    val extraBold: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 26.sp,
        )
    val black: TextStyle =
        TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Black,
            lineHeight = 16.sp,
        )
}
