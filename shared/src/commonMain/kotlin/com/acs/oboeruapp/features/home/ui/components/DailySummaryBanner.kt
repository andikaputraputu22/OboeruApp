package com.acs.oboeruapp.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.deck.domain.model.DailySummary

@Composable
fun DailySummaryBanner(
    summary: DailySummary,
    modifier: Modifier = Modifier,
) {
    val allDone = summary.totalGoals > 0 && summary.completedGoals >= summary.totalGoals
    val accentColor = if (allDone) Color(0xFF4ADE80) else MaterialTheme.colorScheme.primary
    val gradientStart = if (allDone) Color(0xFF162922) else Color(0xFF192233)
    val gradientEnd = Color(0xFF18191F)
    val shape = RoundedCornerShape(22.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(gradientStart, gradientEnd),
                )
            )
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.22f),
                shape = shape,
            )
            .padding(horizontal = 18.dp, vertical = 15.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = if (allDone) "ALL GOALS DONE" else "TODAY'S PROGRESS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = accentColor.copy(alpha = 0.9f),
                )

                Spacer(Modifier.height(4.dp))

                if (summary.totalGoals > 0) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${summary.completedGoals}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 26.sp,
                        )
                        Text(
                            text = " / ${summary.totalGoals}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 2.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (summary.totalGoals == 1) "goal completed" else "goals completed",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 2.dp),
                        )
                    }
                } else {
                    Text(
                        text = "Keep your streak alive",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFB923C).copy(alpha = 0.14f))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFFB923C).copy(alpha = 0.28f),
                        shape = CircleShape,
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "🔥 ${summary.streakDays}d",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFB923C),
                )
            }
        }

        if (summary.totalGoals > 0) {
            Spacer(Modifier.height(12.dp))

            val segmentCount = summary.totalGoals.coerceAtMost(8)
            val filledSegments = if (summary.totalGoals <= segmentCount) {
                summary.completedGoals
            } else {
                ((summary.completedGoals.toFloat() / summary.totalGoals.toFloat()) * segmentCount).toInt()
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                repeat(segmentCount) { index ->
                    val isFilled = index < filledSegments
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(
                                if (isFilled) accentColor else Color.White.copy(alpha = 0.10f)
                            ),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DailySummaryBannerPreview() {
    OboeruTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DailySummaryBanner(
                summary = DailySummary(completedGoals = 1, totalGoals = 3, streakDays = 5),
            )
            DailySummaryBanner(
                summary = DailySummary(completedGoals = 2, totalGoals = 2, streakDays = 12),
            )
        }
    }
}
