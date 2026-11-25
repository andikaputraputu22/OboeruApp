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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_close
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBlockBottomSheet(
    block: UserDeckBlock,
    onDismiss: () -> Unit,
    onSaveDailyTarget: (blockId: String, dailyTarget: Int) -> Unit,
    onRemoveBlock: (blockId: String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val accentColor = getBlockAccentColor(block.topic, block.activityType)
    var editedTarget by remember(block.id, block.dailyTarget) {
        mutableIntStateOf(block.dailyTarget.coerceAtLeast(5))
    }

    val statusSubtitle = when (block.activityType) {
        DeckActivityType.CHART -> "${block.topic.badgeLabel} · Reference table"
        DeckActivityType.REVIEW -> if (block.dueCount <= 0) "All caught up" else "${block.dueCount} items due"
        DeckActivityType.FLASHCARD,
        DeckActivityType.QUIZ -> "${block.topic.badgeLabel} · ${block.completedToday}/${block.dailyTarget} today"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
        ) {
            // Centered Header (consistent with AddBlockBottomSheet & ProfileTopBar)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Manage Block",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                SheetIconCircleButton(
                    icon = Res.drawable.ic_close,
                    contentDescription = "Close",
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }

            Spacer(Modifier.height(16.dp))

            // Block Preview Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
                    .border(
                        width = 1.dp,
                        color = accentColor.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(18.dp),
                    )
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = vectorResource(getBlockIcon(block.activityType)),
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = block.displayTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = statusSubtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (block.isGoalBased) {
                Spacer(Modifier.height(22.dp))

                Text(
                    text = "DAILY GOAL (ITEMS PER DAY)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 4.dp),
                )

                Spacer(Modifier.height(10.dp))

                DailyGoalPresetRow(
                    selectedTarget = editedTarget,
                    accentColor = accentColor,
                    onSelectTarget = { editedTarget = it },
                )

                Spacer(Modifier.height(18.dp))

                val hasChanged = editedTarget != block.dailyTarget
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (hasChanged) Color.White else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable(enabled = hasChanged) {
                            onSaveDailyTarget(block.id, editedTarget)
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (hasChanged) "Save Goal ($editedTarget / day)" else "Save Goal",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasChanged) {
                            Color.Black
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }

                Spacer(Modifier.height(18.dp))
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.08f),
                    thickness = 1.dp,
                )
            }

            Spacer(Modifier.height(18.dp))

            // Remove Action Pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.28f),
                        shape = CircleShape,
                    )
                    .clickable {
                        onRemoveBlock(block.id)
                        onDismiss()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Remove from Home",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Your learning progress stays safe in My Japanese.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
