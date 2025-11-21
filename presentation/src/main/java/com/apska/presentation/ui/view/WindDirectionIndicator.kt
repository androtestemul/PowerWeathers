package com.apska.presentation.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

private const val LAUNCH_ANIMATION_DURATION = 2000
private const val CLICK_ANIMATION_FAST_DURATION = 200
private const val CLICK_ANIMATION_SLOW_DURATION = 1500
private const val FULL_CIRCLE_DEGREES = 360f
private val directions = listOf("N", "E", "S", "W")
private val directionAngles = listOf(0f, 90f, 180f, 270f)

@Composable
fun WindDirectionIndicator(
    degrees: Float,
    modifier: Modifier = Modifier,
    preferredSize: Dp = 120.dp,
    arrowColor: Color = Color.Red,
    circleColor: Color = Color.Gray,
    textColor: Color = Color.Black
) {
    val rotationAnimatable = remember { Animatable(0f) }
    var clickCount by remember { mutableStateOf(0) }

    LaunchedEffect(degrees) {
        if (clickCount == 0) {
            rotationAnimatable.animateTo(
                targetValue = degrees,
                animationSpec = tween(
                    durationMillis = LAUNCH_ANIMATION_DURATION,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    LaunchedEffect(clickCount) {
        if (clickCount > 0) {
            var clickAnimationDuration = CLICK_ANIMATION_FAST_DURATION
            var easing = LinearEasing
            repeat(5) { index ->
                if (index == 4) {
                    clickAnimationDuration = CLICK_ANIMATION_SLOW_DURATION
                    easing = LinearOutSlowInEasing
                }
                rotationAnimatable.animateTo(
                    targetValue = rotationAnimatable.value + FULL_CIRCLE_DEGREES,
                    animationSpec = tween(
                        durationMillis = clickAnimationDuration,
                        easing = easing
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(preferredSize)
                .clickable { clickCount++ },
            contentAlignment = Alignment.Center
        ) {
            WindIndicatorStaticBackground(
                modifier = Modifier.fillMaxSize(),
                circleColor = circleColor,
                textColor = textColor,
                arrowColor = arrowColor
            )

            WindArrow(
                rotationDegrees = { rotationAnimatable.value },
                modifier = Modifier.fillMaxSize(),
                arrowColor = arrowColor
            )

            Text(
                text = "${degrees.toInt()}°",
                color = textColor,
                fontSize = 12.sp
            )
        }
    }

}

@Composable
private fun WindIndicatorStaticBackground(
    modifier: Modifier = Modifier,
    circleColor: Color,
    textColor: Color,
    arrowColor: Color
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResults = remember(textColor) { // Кэшируем результат
        directions.map {
            textMeasurer.measure(
                text = it,
                style = TextStyle(color = textColor, fontSize = 14.sp)
            )
        }
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2 - 10.dp.toPx()

        // Рисуем внешний круг
        drawCircle(
            color = circleColor,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Рисуем внутренний круг
        drawCircle(
            color = circleColor.copy(alpha = 0.1f),
            radius = radius - 5.dp.toPx(),
            center = center
        )

        // Рисуем стороны света
        directions.forEachIndexed { index, _ ->
            val angle = directionAngles[index]
            val textRadius = radius - 15.dp.toPx()
            val x = center.x + textRadius * sin(Math.toRadians(angle.toDouble())).toFloat()
            val y = center.y - textRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val textLayoutResult = textLayoutResults[index]
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x - textLayoutResult.size.width / 2,
                    y - textLayoutResult.size.height / 2
                )
            )
        }

        // Рисуем дополнительные деления
        for (angle in 0 until 360 step 45) {
            if (angle % 90 == 0) continue

            val innerRadius = radius - 8.dp.toPx()
            val outerRadius = radius - 3.dp.toPx()
            val startX = center.x + innerRadius * sin(Math.toRadians(angle.toDouble())).toFloat()
            val startY = center.y - innerRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val endX = center.x + outerRadius * sin(Math.toRadians(angle.toDouble())).toFloat()
            val endY = center.y - outerRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
            drawLine(
                color = circleColor,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Рисуем центральную точку (основание)
        drawCircle(
            color = arrowColor,
            radius = 5.dp.toPx(),
            center = center
        )
    }
}

@Composable
private fun WindArrow(
    rotationDegrees: () -> Float,
    modifier: Modifier = Modifier,
    arrowColor: Color
) {
    Canvas(modifier = modifier) {
        rotate(degrees = rotationDegrees(), pivot = center) {
            val radius = size.width / 2 - 10.dp.toPx()
            val arrowLength = radius - 25.dp.toPx()
            val arrowStartY = center.y + (radius - 25.dp.toPx())
            val arrowEndY = center.y - arrowLength

            // Линия стрелки
            drawLine(
                color = arrowColor,
                start = Offset(center.x, arrowStartY),
                end = Offset(center.x, arrowEndY),
                strokeWidth = 3.dp.toPx()
            )

            // Наконечник стрелки
            val arrowHeadSize = 10.dp.toPx()
            val path = Path().apply {
                moveTo(center.x, arrowEndY - 5.dp.toPx())
                lineTo(center.x - arrowHeadSize / 2, arrowEndY + arrowHeadSize)
                lineTo(center.x + arrowHeadSize / 2, arrowEndY + arrowHeadSize)
                close()
            }

            drawPath(
                path = path,
                color = arrowColor
            )
        }
    }
}

@Preview
@Composable
fun WindDirectionPreview() {
    WindDirectionIndicator(
        degrees = 45f, // Северо-восток
        preferredSize = 150.dp,
        arrowColor = Color.Blue,
        textColor = Color.DarkGray
    )
}
