package com.polstat.uas_ppk.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.polstat.uas_ppk.ui.theme.Quicksand

@Composable
fun ActionButton(text: String, bgColor: Color, txtColor: Color = Color.White, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = bgColor)
    ) {
        Text(text, fontFamily = Quicksand, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = txtColor)
    }
}