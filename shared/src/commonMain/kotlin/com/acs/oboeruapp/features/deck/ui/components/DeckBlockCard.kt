package com.acs.oboeruapp.features.deck.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.DeckTopic
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_book
import oboeruapp.shared.generated.resources.ic_check
import oboeruapp.shared.generated.resources.ic_grammar
import oboeruapp.shared.generated.resources.ic_play
import oboeruapp.shared.generated.resources.ic_play_outline
import oboeruapp.shared.generated.resources.ic_timer
import oboeruapp.shared.generated.resources.ic_vocabulary
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

fun getBlockAccentColor(topic: DeckTopic, activityType: DeckActivityType): Color =
    when {
        activityType == DeckActivityType.REVIEW || topic == DeckTopic.ALL_REVIEW -> Color(0xFFFB923C)
        topic == DeckTopic.HIRAGANA && activityType == DeckActivityType.CHART -> Color(0xFF38BDF8)
        topic == DeckTopic.HIRAGANA && activityType == DeckActivityType.FLASHCARD -> Color(0xFF818CF8)
        topic == DeckTopic.HIRAGANA && activityType == DeckActivityType.QUIZ -> Color(0xFF4ADE80)
        topic == DeckTopic.KATAKANA && activityType == DeckActivityType.CHART -> Color(0xFF22D3EE)
        topic == DeckTopic.KATAKANA && activityType == DeckActivityType.FLASHCARD -> Color(0xFFA78BFA)
        topic == DeckTopic.KATAKANA && activityType == DeckActivityType.QUIZ -> Color(0xFF2DD4BF)
        else -> Color(0xFF38BDF8)
    }

fun getBlockIcon(activityType: DeckActivityType): DrawableResource =
    when (activityType) {
        DeckActivityType.CHART -> Res.drawable.ic_book
        DeckActivityType.FLASHCARD -> Res.drawable.ic_vocabulary
        DeckActivityType.QUIZ -> Res.drawable.ic_grammar
        DeckActivityType.REVIEW -> Res.drawable.ic_timer
    }

@Composable
fun DeckBlockCard(
    block: UserDeckBlock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = getBlockAccentColor(block.topic, block.activityType)
    val icon = getBlockIcon(block.activityType)

    val (subtitle, badgeText) = when (block.activityType) {
        DeckActivityType.CHART -> {
            "${block.topic.badgeLabel} · 104 characters" to "Reference table"
        }

        DeckActivityType.REVIEW -> {
            if (block.dueCount <= 0) {
                "All caught up for now" to "Up to date"
            } else {
                val unit = if (block.dueCount == 1) "item" else "items"
                "${block.dueCount} $unit due" to "Start review"
            }
        }

        DeckActivityType.FLASHCARD,
        DeckActivityType.QUIZ -> {
            val sub = "${block.topic.badgeLabel} · ${block.completedToday}/${block.dailyTarget} today"
            val badge = if (block.isCompletedToday) {
                "✓ Done today"
            } else {
                val remaining = (block.dailyTarget - block.completedToday).coerceAtLeast(0)
                "$remaining left today"
            }
            sub to badge
        }
    }

    val progress = block.progressFraction
    val cardShape = RoundedCornerShape(28.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = cardShape,
                ambientColor = Color.Black.copy(alpha = 0.30f),
                spotColor = Color.Black.copy(alpha = 0.40f),
            )
            .clip(cardShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = if (block.isCompletedToday) {
                    accentColor.copy(alpha = 0.25f)
                } else {
                    Color.White.copy(alpha = 0.07f)
                },
                shape = cardShape,
            )
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = vectorResource(icon),
            contentDescription = null,
            tint = accentColor.copy(alpha = 0.07f),
            modifier = Modifier
                .size(76.dp)
                .offset(x = (-8).dp, y = 14.dp)
                .align(Alignment.BottomStart)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = vectorResource(icon),
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(32.dp),
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = block.displayTitle,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (block.isCompletedToday) {
                                accentColor.copy(alpha = 0.14f)
                            } else {
                                Color.Black.copy(alpha = 0.32f)
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (block.isCompletedToday) {
                                accentColor.copy(alpha = 0.35f)
                            } else {
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
                            },
                            shape = CircleShape,
                        )
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (block.isCompletedToday) {
                            accentColor
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            if (progress != null) {
                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(38.dp),
                        color = accentColor,
                        trackColor = accentColor.copy(alpha = 0.16f),
                        strokeWidth = 3.dp,
                        strokeCap = StrokeCap.Round,
                    )
                    Icon(
                        imageVector = vectorResource(
                            if (block.isCompletedToday) Res.drawable.ic_check else Res.drawable.ic_play
                        ),
                        contentDescription = if (block.isCompletedToday) "Completed" else "Start",
                        tint = accentColor,
                        modifier = Modifier
                            .size(14.dp)
                            .offset(x = if (block.isCompletedToday) 0.dp else 1.dp),
                    )
                }
            } else {
                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_play_outline),
                        contentDescription = "Start",
                        tint = accentColor,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}
