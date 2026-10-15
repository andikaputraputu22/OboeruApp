package com.acs.oboeruapp.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ConfettiDot(
    val color: Color,
    val size: Dp,
    val offsetX: Dp,
    val offsetY: Dp
)

val confettiDots = listOf(
    ConfettiDot(Color(0xFFE8B84A), 10.dp, offsetX = (-10).dp, offsetY = (-90).dp),
    ConfettiDot(Color(0xFFB8C4D9), 6.dp, offsetX = 50.dp, offsetY = (-75).dp),
    ConfettiDot(Color(0xFF8FA8D4), 10.dp, offsetX = (-85).dp, offsetY = (-40).dp),
    ConfettiDot(Color(0xFF8FD4BE), 8.dp, offsetX = 90.dp, offsetY = (-20).dp),
    ConfettiDot(Color(0xFFB8C4D9), 6.dp, offsetX = (-95).dp, offsetY = 15.dp),
    ConfettiDot(Color(0xFFE8A4B0), 8.dp, offsetX = (-75).dp, offsetY = 55.dp),
    ConfettiDot(Color(0xFFE8B84A), 8.dp, offsetX = 95.dp, offsetY = 35.dp),
    ConfettiDot(Color(0xFF8FA8D4), 7.dp, offsetX = 55.dp, offsetY = 70.dp),
    ConfettiDot(Color(0xFF8FD4BE), 6.dp, offsetX = (-30).dp, offsetY = 80.dp),
)