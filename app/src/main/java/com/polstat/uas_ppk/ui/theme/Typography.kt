package com.polstat.uas_ppk.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.polstat.uas_ppk.R

// Define Quicksand FontFamily
val Quicksand = FontFamily(
    Font(R.font.quicksand_regular, FontWeight.Normal),
    Font(R.font.quicksand_medium, FontWeight.Medium),
    Font(R.font.quicksand_semibold, FontWeight.SemiBold),
    Font(R.font.quicksand_light, FontWeight.Light),
    Font(R.font.quicksand_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = Quicksand),
    displayMedium = Typography().displayMedium.copy(fontFamily = Quicksand),
    displaySmall = Typography().displaySmall.copy(fontFamily = Quicksand),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = Quicksand),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = Quicksand),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = Quicksand),
    titleLarge = Typography().titleLarge.copy(fontFamily = Quicksand),
    titleMedium = Typography().titleMedium.copy(fontFamily = Quicksand),
    titleSmall = Typography().titleSmall.copy(fontFamily = Quicksand),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = Quicksand),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = Quicksand),
    bodySmall = Typography().bodySmall.copy(fontFamily = Quicksand),
    labelLarge = Typography().labelLarge.copy(fontFamily = Quicksand),
    labelMedium = Typography().labelMedium.copy(fontFamily = Quicksand),
    labelSmall = Typography().labelSmall.copy(fontFamily = Quicksand)
)
