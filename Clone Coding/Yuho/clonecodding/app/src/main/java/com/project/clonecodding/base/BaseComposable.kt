package com.project.clonecodding.base

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SimpleCircle(modifier: Modifier = Modifier, color: Color = Color(0xFFD9D9D9)) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = color,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = size.minDimension / 4
        )
    }
}

@Composable
fun SimpleLine(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFD9D9D9),
    stroke: Dp = 1.dp,
    orientation: Orientation = Orientation.Horizontal
) {
    if(orientation == Orientation.Horizontal){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color)
        )
    }else{
        Box(
            modifier = modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(color)
        )
    }

}
