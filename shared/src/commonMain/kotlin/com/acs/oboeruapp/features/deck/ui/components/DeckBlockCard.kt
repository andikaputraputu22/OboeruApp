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
import com.acs.oboeruapp.features.deck.domain.model.DeckBlock
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_play
import oboeruapp.shared.generated.resources.ic_play_outline
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.abs

/**
 * Returns a distinct neon/pastel accent color for the block,
 * matching the color palette in sample-block.jpeg.
 */
fun getBlockAccentColor(blockId: String): Color {
    return when (blockId.lowercase()) {
        "vocab", "vocabulary" -> Color(0xFF38BDF8) // Cyan / Sky Blue (Card 1)
        "kanji" -> Color(0xFF818CF8)               // Indigo / Periwinkle (Card 2)
        "grammar" -> Color(0xFF4ADE80)             // Emerald / Mint (Card 3/4)
        "review", "timer" -> Color(0xFFFB923C)     // Warm Coral / Orange (Card 6)
        else -> {
            val palette = listOf(
                Color(0xFF38BDF8),
                Color(0xFF818CF8),
                Color(0xFF4ADE80),
                Color(0xFFFB923C),
                Color(0xFFA78BFA),
                Color(0xFFF472B6)
            )
            palette[abs(blockId.hashCode()) % palette.size]
        }
    }
}

@Composable
fun DeckBlockCard(
    block: DeckBlock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = getBlockAccentColor(block.id)

    val (subtitle, badgeText, progress) = when (block) {
        is DeckBlock.ProgressBlock -> {
            val sub = "${block.level} · Goal: ${block.target} items"
            val badge = if (block.current >= block.target) {
                "Completed"
            } else {
                "${block.target - block.current} items left"
            }
            val prog = (block.current.toFloat() / block.target.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
            Triple(sub, badge, prog)
        }
        is DeckBlock.SimpleBlock -> {
            val badge = if (block.subtitle.contains("due", ignoreCase = true)) {
                "Start review"
            } else {
                "Tap to start"
            }
            Triple(block.subtitle, badge, null)
        }
    }

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
                color = Color.White.copy(alpha = 0.07f),
                shape = cardShape,
            )
            .clickable(onClick = onClick)
    ) {
        // Subtle background icon watermark (inspired by sample-block.jpeg)
        Icon(
            imageVector = vectorResource(block.icon),
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
            // Left main icon
            Icon(
                imageVector = vectorResource(block.icon),
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(32.dp),
            )

            Spacer(Modifier.width(16.dp))

            // Center info column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = block.title,
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

                // Pill Badge (status / timer tag)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.32f))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
                            shape = CircleShape,
                        )
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Right action / play button
            if (progress != null) {
                // Card with circular progress ring + solid play triangle (Card 2 style)
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
                        imageVector = vectorResource(Res.drawable.ic_play),
                        contentDescription = "Start",
                        tint = accentColor,
                        modifier = Modifier
                            .size(14.dp)
                            .offset(x = 1.dp),
                    )
                }
            } else {
                // Card with outline play triangle (Card 1, 3, 4, 5, 6 style)
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
