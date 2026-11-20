package com.acs.oboeruapp.features.onboarding.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_book
import oboeruapp.shared.generated.resources.ic_check
import oboeruapp.shared.generated.resources.ic_grammar
import oboeruapp.shared.generated.resources.ic_timer
import org.jetbrains.compose.resources.vectorResource

/**
 * Page 1 Illustration: 3 Floating Japanese Character Cards
 */
@Composable
fun JourneyIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center,
    ) {
        // 1. Top-Left Floating Card ("あ")
        Box(
            modifier = Modifier
                .offset(x = (-95).dp, y = (-45).dp)
                .rotate(-13f)
                .size(width = 100.dp, height = 82.dp)
                .shadow(16.dp, RoundedCornerShape(18.dp), spotColor = Color.Black.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.20f), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "あ",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            // Cyan accent bar at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .width(44.dp)
                    .height(3.5.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF38BDF8)),
            )
        }

        // 2. Top-Right Floating Card ("言葉")
        Box(
            modifier = Modifier
                .offset(x = 95.dp, y = (-55).dp)
                .rotate(14f)
                .size(width = 110.dp, height = 82.dp)
                .shadow(16.dp, RoundedCornerShape(18.dp), spotColor = Color.Black.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, Color(0xFFFB923C).copy(alpha = 0.20f), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "言葉",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            // Orange accent bar at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .width(48.dp)
                    .height(3.5.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFB923C)),
            )
        }

        // 3. Main Center Floating Card ("日本", larger)
        Box(
            modifier = Modifier
                .offset(y = 25.dp)
                .rotate(-4f)
                .size(width = 180.dp, height = 125.dp)
                .shadow(24.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.65f))
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, Color(0xFF4ADE80).copy(alpha = 0.22f), RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "日本",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            // Green accent bar at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .width(80.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4ADE80)),
            )
        }
    }
}

/**
 * Page 2 Illustration: Dynamic Floating Learning Space Category Cards
 */
@Composable
fun LearningSpaceIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center,
    ) {
        // 1. Top-Left: Vocabulary Card (Cyan, tilted counter-clockwise)
        CategoryFloatingCard(
            title = "Vocabulary",
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_book),
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(28.dp),
                )
            },
            accentColor = Color(0xFF38BDF8),
            modifier = Modifier
                .offset(x = (-66).dp, y = (-54).dp)
                .rotate(-8f)
                .size(width = 114.dp, height = 104.dp),
        )

        // 2. Top-Right: Kanji Card (Purple, staggered lower and tilted clockwise)
        CategoryFloatingCard(
            title = "Kanji",
            icon = {
                Text(
                    text = "漢",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC084FC),
                )
            },
            accentColor = Color(0xFFC084FC),
            modifier = Modifier
                .offset(x = 62.dp, y = (-32).dp)
                .rotate(9f)
                .size(width = 112.dp, height = 106.dp),
        )

        // 3. Bottom-Left: Grammar Card (Green, tilted clockwise)
        CategoryFloatingCard(
            title = "Grammar",
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_grammar),
                    contentDescription = null,
                    tint = Color(0xFF4ADE80),
                    modifier = Modifier.size(26.dp),
                )
            },
            accentColor = Color(0xFF4ADE80),
            modifier = Modifier
                .offset(x = (-52).dp, y = 54.dp)
                .rotate(5f)
                .size(width = 110.dp, height = 102.dp),
        )

        // 4. Bottom-Right: Review Card (Orange, staggered lower and tilted counter-clockwise)
        CategoryFloatingCard(
            title = "Review",
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_timer),
                    contentDescription = null,
                    tint = Color(0xFFFB923C),
                    modifier = Modifier.size(26.dp),
                )
            },
            accentColor = Color(0xFFFB923C),
            modifier = Modifier
                .offset(x = 68.dp, y = 74.dp)
                .rotate(-7f)
                .size(width = 114.dp, height = 104.dp),
        )
    }
}

@Composable
private fun CategoryFloatingCard(
    title: String,
    icon: @Composable () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.55f))
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.surface)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.08f),
                        Color.Transparent,
                    ),
                ),
            )
            .border(1.dp, accentColor.copy(alpha = 0.24f), RoundedCornerShape(22.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            icon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentColor,
            )
        }
    }
}

/**
 * Page 3 Illustration: 3 Floating Progress Indicator Cards
 */
@Composable
fun ProgressIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center,
    ) {
        // 1. Top-Left Card: Daily Goal (Cyan)
        Box(
            modifier = Modifier
                .offset(x = (-78).dp, y = (-46).dp)
                .rotate(-8f)
                .size(width = 112.dp, height = 108.dp)
                .shadow(16.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.55f))
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF38BDF8).copy(alpha = 0.08f),
                            Color.Transparent,
                        ),
                    ),
                )
                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.25f), RoundedCornerShape(22.dp))
                .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularGauge(
                    sweepAngle = 280f,
                    accentColor = Color(0xFF38BDF8),
                ) {
                    Text(
                        text = "80%",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Daily Goal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF38BDF8),
                )
            }
        }

        // 2. Top-Right Card: Habit Streak (Orange)
        Box(
            modifier = Modifier
                .offset(x = 78.dp, y = (-46).dp)
                .rotate(8f)
                .size(width = 112.dp, height = 108.dp)
                .shadow(16.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.55f))
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFB923C).copy(alpha = 0.08f),
                            Color.Transparent,
                        ),
                    ),
                )
                .border(1.dp, Color(0xFFFB923C).copy(alpha = 0.25f), RoundedCornerShape(22.dp))
                .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularGauge(
                    sweepAngle = 310f,
                    accentColor = Color(0xFFFB923C),
                ) {
                    Text(
                        text = "7d",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🔥 Streak",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFB923C),
                )
            }
        }

        // 3. Center-Bottom Hero Card: Cards Mastered (Green)
        Box(
            modifier = Modifier
                .offset(y = 48.dp)
                .rotate(-3f)
                .size(width = 186.dp, height = 104.dp)
                .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.6f))
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4ADE80).copy(alpha = 0.08f),
                            Color.Transparent,
                        ),
                    ),
                )
                .border(1.dp, Color(0xFF4ADE80).copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularGauge(
                    sweepAngle = 360f,
                    accentColor = Color(0xFF4ADE80),
                    size = 52.dp,
                    strokeWidth = 5.dp,
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_check),
                        contentDescription = null,
                        tint = Color(0xFF4ADE80),
                        modifier = Modifier.size(18.dp),
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "10 / 10",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Session Target",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "✓ Mastered",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4ADE80),
                    )
                }
            }
        }
    }
}

@Composable
private fun CircularGauge(
    sweepAngle: Float,
    accentColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    strokeWidth: Dp = 4.5.dp,
    centerContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = strokeWidth.toPx()
            // Track background
            drawArc(
                color = Color.White.copy(alpha = 0.08f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
            // Progress Arc
            drawArc(
                color = accentColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }
        centerContent()
    }
}
