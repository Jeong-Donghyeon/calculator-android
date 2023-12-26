package dev.donghyeon.calculator.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.R

val PretendardFamily = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin),
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_black, FontWeight.Black)
)

val TsThin = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.Thin,
    lineHeight = 16.sp
)
val TsExtraLight = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.ExtraLight,
    lineHeight = 16.sp
)
val TsLight = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.Light,
    lineHeight = 16.sp
)
val TsRegular = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp
)
val TsMedium = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.sp
)
val TsSemiBold = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 16.sp
)
val TsBold = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.Bold,
    lineHeight = 16.sp
)
val TsExtraBold = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.ExtraBold,
    lineHeight = 26.sp
)
val TsBlack = TextStyle(
    fontFamily = PretendardFamily,
    fontWeight = FontWeight.Black,
    lineHeight = 16.sp
)
