package com.acs.oboeruapp.features.kana.flashcard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.core.domain.srs.SrsRating
import com.acs.oboeruapp.features.kana.flashcard.SrsIntervalHints

@Composable
fun SrsRatingBar(
    isFlipped: Boolean,
    isSubmitting: Boolean,
    intervalHints: SrsIntervalHints?,
    goodAccentColor: Color,
    onRevealClick: () -> Unit,
    onRateClick: (SrsRating) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (!isFlipped) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(enabled = !isSubmitting, onClick = onRevealClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Reveal Answer",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isSubmitting) 0.55f else 1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SrsRatingButton(
                    title = "Again",
                    intervalHint = intervalHints?.againLabel ?: "10m",
                    accentColor = Color(0xFFF87171),
                    enabled = !isSubmitting,
                    onClick = { onRateClick(SrsRating.AGAIN) },
                    modifier = Modifier.weight(1f),
                )

                SrsRatingButton(
                    title = "Good",
                    intervalHint = intervalHints?.goodLabel ?: "1d",
                    accentColor = goodAccentColor,
                    enabled = !isSubmitting,
                    onClick = { onRateClick(SrsRating.GOOD) },
                    modifier = Modifier.weight(1f),
                )

                SrsRatingButton(
                    title = "Easy",
                    intervalHint = intervalHints?.easyLabel ?: "7d",
                    accentColor = Color(0xFF4ADE80),
                    enabled = !isSubmitting,
                    onClick = { onRateClick(SrsRating.EASY) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SrsRatingButton(
    title: String,
    intervalHint: String,
    accentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier
            .height(62.dp)
            .clip(shape)
            .background(accentColor.copy(alpha = 0.14f))
            .border(1.dp, accentColor.copy(alpha = 0.40f), shape)
            .clickable(enabled = enabled, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = intervalHint,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = accentColor.copy(alpha = 0.80f),
        )
    }
}
