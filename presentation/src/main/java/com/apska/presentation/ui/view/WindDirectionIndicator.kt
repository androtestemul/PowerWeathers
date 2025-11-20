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
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WindDirectionIndicator(
    degrees: Float,
    modifier: Modifier = Modifier,
    preferredSize: Dp = 120.dp,
    arrowColor: Color = Color.Red,
    circleColor: Color = Color.Gray,
    textColor: Color = Color.Black
) {
    val textMeasurer = rememberTextMeasurer()
    val rotationAnimatable = remember { Animatable(0f) }
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    LaunchedEffect(degrees) {
        rotationAnimatable.animateTo(
            targetValue = degrees,
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(preferredSize)
                .clickable {
                    coroutineScope.launch {
                        var clickAnimationDuration = 200
                        var easing = LinearEasing
                        repeat(5) { index ->
                            if (index == 4) {
                                clickAnimationDuration = 1500
                                easing = LinearOutSlowInEasing
                            }
                            rotationAnimatable.animateTo(
                                targetValue = rotationAnimatable.value + 360f,
                                animationSpec = tween(
                                    durationMillis = clickAnimationDuration,
                                    easing = easing
                                )
                            )
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(preferredSize.toPx() / 2, preferredSize.toPx() / 2)
                val radius = preferredSize.toPx() / 2 - 10.dp.toPx()

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
                val directions = listOf("N", "E", "S", "W")
                val directionAngles = listOf(0f, 90f, 180f, 270f)

                directions.forEachIndexed { index, direction ->
                    val angle = directionAngles[index]
                    val textRadius = radius - 15.dp.toPx()

                    val x = center.x + textRadius * sin(Math.toRadians(angle.toDouble())).toFloat()
                    val y = center.y - textRadius * cos(Math.toRadians(angle.toDouble())).toFloat()

                    val textLayoutResult = textMeasurer.measure(
                        text = direction,
                        style = TextStyle(
                            color = textColor,
                            fontSize = 14.sp
                        )
                    )

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
                    if (angle % 90 == 0) continue // Пропускаем основные направления

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

                // Рисуем стрелку направления ветра
                rotate(degrees = rotationAnimatable.value) {
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
                    drawPath(
                        path = Path().apply {
                            moveTo(center.x, arrowEndY - 5.dp.toPx())
                            lineTo(center.x - arrowHeadSize / 2, arrowEndY + arrowHeadSize)
                            lineTo(center.x + arrowHeadSize / 2, arrowEndY + arrowHeadSize)
                            close()
                        },
                        color = arrowColor
                    )

                    // Основание стрелки
                    drawCircle(
                        color = arrowColor,
                        radius = 5.dp.toPx(),
                        center = center
                    )
                }

                // Рисуем центральную точку
                drawCircle(
                    color = arrowColor,
                    radius = 2.dp.toPx(),
                    center = center
                )
            }

            // Отображаем значение в градусах
            Text(
                text = "${degrees.toInt()}°",
                color = textColor,
                fontSize = 12.sp
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
