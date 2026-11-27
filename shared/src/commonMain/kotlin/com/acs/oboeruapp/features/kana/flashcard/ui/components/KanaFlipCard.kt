package com.acs.oboeruapp.features.kana.flashcard.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript

@Composable
fun KanaFlipCard(
    itemWithProgress: KanaItemWithProgress,
    isFlipped: Boolean,
    accentColor: Color,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "kana_flashcard_flip",
    )
    val density = LocalDensity.current.density
    val cardShape = RoundedCornerShape(28.dp)
    val showingBack = rotation > 90f

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(cardShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF22242B),
                        Color(0xFF1A1B21),
                    )
                )
            )
            .border(
                width = 1.dp,
                color = if (showingBack) {
                    accentColor.copy(alpha = 0.28f)
                } else {
                    Color.White.copy(alpha = 0.08f)
                },
                shape = cardShape,
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onFlip,
            ),
    ) {
        if (!showingBack) {
            CardFrontFace(
                itemWithProgress = itemWithProgress,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            CardBackFace(
                itemWithProgress = itemWithProgress,
                accentColor = accentColor,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
            )
        }
    }
}

@Composable
private fun CardFrontFace(
    itemWithProgress: KanaItemWithProgress,
    modifier: Modifier = Modifier,
) {
    val item = itemWithProgress.item
    val scriptLabel = if (item.script == KanaScript.HIRAGANA) "Hiragana" else "Katakana"
    val categoryLabel = when (item.category) {
        KanaCategory.BASIC -> "Basic"
        KanaCategory.DAKUTEN -> "Dakuten"
        KanaCategory.YOON -> "Yoon"
    }
    val (statusLabel, statusColor) = when (itemWithProgress.status) {
        ProgressStatus.MASTERED -> "Mastered" to Color(0xFF4ADE80)
        ProgressStatus.LEARNING -> "Learning" to Color(0xFFF5C842)
        ProgressStatus.NEW -> "New" to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$scriptLabel · $categoryLabel",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            StatusBadge(label = statusLabel, color = statusColor)
        }

        Text(
            text = item.character,
            fontSize = if (item.character.length > 1) 72.sp else 92.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )

        Text(
            text = "Tap card to reveal",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
        )
    }
}

@Composable
private fun CardBackFace(
    itemWithProgress: KanaItemWithProgress,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    val item = itemWithProgress.item
    val scriptLabel = if (item.script == KanaScript.HIRAGANA) "Hiragana" else "Katakana"
    val categoryLabel = when (item.category) {
        KanaCategory.BASIC -> "Basic"
        KanaCategory.DAKUTEN -> "Dakuten"
        KanaCategory.YOON -> "Yoon"
    }

    Column(
        modifier = modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$scriptLabel · $categoryLabel",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.16f))
                    .border(1.dp, accentColor.copy(alpha = 0.30f), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    text = item.character,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.romaji,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )

            if (!item.exampleWord.isNullOrBlank()) {
                Spacer(Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.50f))
                        .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    Text(
                        text = "EXAMPLE WORD",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f),
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = item.exampleWord,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (!item.exampleMeaning.isNullOrBlank()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = item.exampleMeaning,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        val footerText = if (itemWithProgress.correctCount > 0 || itemWithProgress.wrongCount > 0) {
            "History · ${itemWithProgress.correctCount} correct · ${itemWithProgress.wrongCount} missed"
        } else {
            "How well did you remember?"
        }

        Text(
            text = footerText,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StatusBadge(
    label: String,
    color: Color,
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.28f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = color,
        )
    }
}
