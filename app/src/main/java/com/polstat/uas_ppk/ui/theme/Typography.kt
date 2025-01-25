package com.polstat.uas_ppk.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.polstat.uas_ppk.R

// Define Quicksand FontFamily
val Quicksand = FontFamily(
    Font(R.font.quicksand_regular, FontWeight.Normal),
    Font(R.font.quicksand_medium, FontWeight.Medium),
    Font(R.font.quicksand_semibold, FontWeight.SemiBold),
    Font(R.font.quicksand_light, FontWeight.Light),
    Font(R.font.quicksand_bold, FontWeight.Bold)
)

// Apply Quicksand to Material 3 Typography
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)
