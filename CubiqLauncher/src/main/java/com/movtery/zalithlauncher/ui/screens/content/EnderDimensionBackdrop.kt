package com.movtery.zalithlauncher.ui.screens.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * Original End-inspired launcher artwork drawn with Compose primitives.
 * No external background image is required.
 */
@Composable
fun EnderDimensionBackdrop(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val unit = min(w, h)

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF160D2B),
                    Color(0xFF38205C),
                    Color(0xFF211332),
                    Color(0xFF090711)
                ),
                startY = 0f,
                endY = h
            )
        )

        // Distant violet nebula glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x995F32A0), Color(0x443B1C70), Color.Transparent),
                center = Offset(w * 0.76f, h * 0.34f),
                radius = unit * 0.72f
            ),
            radius = unit * 0.72f,
            center = Offset(w * 0.76f, h * 0.34f)
        )

        // Star field: fixed positions keep the artwork stable during recomposition.
        val stars = listOf(
            0.05f to 0.12f, 0.12f to 0.28f, 0.19f to 0.08f, 0.27f to 0.19f,
            0.34f to 0.07f, 0.42f to 0.24f, 0.51f to 0.10f, 0.59f to 0.20f,
            0.67f to 0.08f, 0.83f to 0.12f, 0.91f to 0.25f, 0.96f to 0.08f,
            0.08f to 0.43f, 0.23f to 0.36f, 0.38f to 0.42f, 0.55f to 0.38f,
            0.72f to 0.48f, 0.89f to 0.42f
        )
        stars.forEachIndexed { index, point ->
            val radius = if (index % 4 == 0) 2.2f else 1.2f
            drawCircle(
                color = Color(0xFFDCCBFF).copy(alpha = if (index % 3 == 0) 0.9f else 0.55f),
                radius = radius.dp.toPx(),
                center = Offset(w * point.first, h * point.second)
            )
        }

        // The giant eclipse portal, intentionally offset to the upper-right.
        val portal = Offset(w * 0.78f, h * 0.29f)
        val portalRadius = unit * 0.205f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x00E9B8FF), Color(0x668D43D7), Color.Transparent),
                center = portal,
                radius = portalRadius * 1.8f
            ),
            radius = portalRadius * 1.8f,
            center = portal
        )
        for (i in 5 downTo 1) {
            drawCircle(
                color = Color(0xFFCA8BFF).copy(alpha = 0.10f + i * 0.025f),
                radius = portalRadius * (1f + i * 0.075f),
                center = portal,
                style = Stroke(width = (i * 2.2f).dp.toPx())
            )
        }
        drawCircle(
            color = Color(0xFFF0C9FF),
            radius = portalRadius,
            center = portal,
            style = Stroke(width = 4.dp.toPx())
        )
        drawCircle(
            color = Color(0xFF100A1B),
            radius = portalRadius - 5.dp.toPx(),
            center = portal
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF5B2A86), Color.Transparent),
                center = Offset(portal.x - portalRadius * 0.9f, portal.y + portalRadius * 0.75f),
                radius = portalRadius * 0.7f
            ),
            radius = portalRadius * 0.7f,
            center = Offset(portal.x - portalRadius * 0.9f, portal.y + portalRadius * 0.75f)
        )

        // End-city silhouettes in the horizon.
        val towerColor = Color(0xFF100B1B)
        val towers = listOf(
            floatArrayOf(0.09f, 0.58f, 0.09f, 0.30f),
            floatArrayOf(0.22f, 0.62f, 0.12f, 0.43f),
            floatArrayOf(0.39f, 0.61f, 0.07f, 0.25f),
            floatArrayOf(0.50f, 0.62f, 0.10f, 0.37f),
            floatArrayOf(0.64f, 0.62f, 0.07f, 0.23f)
        )
        towers.forEachIndexed { index, tower ->
            val left = w * tower[0]
            val top = h * tower[1]
            val tw = w * tower[2]
            val th = h * tower[3]
            drawRect(towerColor, Offset(left, top - th), Size(tw, th))
            drawRect(Color(0xFF241536), Offset(left - 2.dp.toPx(), top - th), Size(tw + 4.dp.toPx(), 4.dp.toPx()))
            if (index % 2 == 0) {
                drawRect(Color(0xFFB77BE8).copy(alpha = 0.45f), Offset(left + tw * 0.72f, top - th * 0.72f), Size(2.dp.toPx(), th * 0.35f))
            }
        }

        // Broken, block-like End island foreground.
        val ground = Path().apply {
            moveTo(0f, h * 0.72f)
            lineTo(w * 0.13f, h * 0.70f)
            lineTo(w * 0.22f, h * 0.73f)
            lineTo(w * 0.34f, h * 0.71f)
            lineTo(w * 0.47f, h * 0.75f)
            lineTo(w * 0.60f, h * 0.72f)
            lineTo(w * 0.76f, h * 0.75f)
            lineTo(w * 0.88f, h * 0.71f)
            lineTo(w, h * 0.74f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = ground,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF30243B), Color(0xFF100D18)),
                startY = h * 0.70f,
                endY = h
            )
        )
        drawLine(
            color = Color(0xFFB78AD6).copy(alpha = 0.35f),
            start = Offset(0f, h * 0.72f),
            end = Offset(w, h * 0.74f),
            strokeWidth = 2.dp.toPx()
        )
        // Dark overlay keeps launcher controls readable over the artwork.
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0x33100918), Color(0xCC090711)),
                startY = h * 0.25f,
                endY = h
            )
        )
    }
}
