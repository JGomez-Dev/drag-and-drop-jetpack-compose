package com.example.drap_and_drop_jetpack_compose.model

import androidx.compose.ui.graphics.Color

data class Particle(
    val color: Color,
    val x: Int,
    val y: Int,
    val radius: Float
)