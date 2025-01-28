package com.polstat.uas_ppk.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.uas_ppk.ui.theme.Quicksand

@Composable
fun TableHeaderItem(text: String, modifier: Modifier) {
    Text(text, fontFamily = Quicksand, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp, modifier = modifier)
    Spacer(modifier = Modifier.width(4.dp))
}

@Composable
fun TableCell(text: String, modifier: Modifier) {
    Text(
        text,
        fontSize = 14.sp,
        color = Color.White,
        fontFamily = Quicksand,
        modifier = modifier
    )
    Spacer(modifier = Modifier.width(4.dp))
}