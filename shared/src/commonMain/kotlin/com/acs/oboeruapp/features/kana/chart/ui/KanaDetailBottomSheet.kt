package com.acs.oboeruapp.features.kana.chart.ui

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.features.deck.ui.components.SheetIconCircleButton
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_close

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanaDetailBottomSheet(
    itemWithProgress: KanaItemWithProgress,
    onDismiss: () -> Unit,
    onToggleMastery: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val item = itemWithProgress.item
    val isMastered = itemWithProgress.status == ProgressStatus.MASTERED
    val scriptAccent = if (item.script == KanaScript.HIRAGANA) Color(0xFF38BDF8) else Color(0xFFA78BFA)

    val (statusLabel, statusColor) = when (itemWithProgress.status) {
        ProgressStatus.MASTERED -> "Mastered" to Color(0xFF4ADE80)
        ProgressStatus.LEARNING -> "Learning" to Color(0xFFF5C842)
        ProgressStatus.NEW -> "New" to MaterialTheme.colorScheme.onSurfaceVariant
    }

    val categoryLabel = when (item.category) {
        KanaCategory.BASIC -> "Basic"
        KanaCategory.DAKUTEN -> "Dakuten"
        KanaCategory.YOON -> "Yoon"
    }

    val scriptLabel = if (item.script == KanaScript.HIRAGANA) "Hiragana" else "Katakana"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Centered Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "$scriptLabel · $categoryLabel",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                SheetIconCircleButton(
                    icon = Res.drawable.ic_close,
                    contentDescription = "Close",
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }

            Spacer(Modifier.height(16.dp))

            // Hero Character Showcase
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
                    .border(
                        width = 1.dp,
                        color = statusColor.copy(alpha = if (isMastered) 0.35f else 0.14f),
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(vertical = 24.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = item.character,
                        fontSize = if (item.character.length > 1) 56.sp else 68.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 76.sp,
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(scriptAccent.copy(alpha = 0.16f))
                                .padding(horizontal = 14.dp, vertical = 5.dp),
                        ) {
                            Text(
                                text = item.romaji,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = scriptAccent,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(statusColor.copy(alpha = 0.14f))
                                .border(1.dp, statusColor.copy(alpha = 0.30f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 5.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(statusColor)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = statusLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = statusColor,
                                )
                            }
                        }
                    }
                }
            }

            if (!item.exampleWord.isNullOrBlank()) {
                Spacer(Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.40f))
                        .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    Text(
                        text = "EXAMPLE WORD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = item.exampleWord,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    if (!item.exampleMeaning.isNullOrBlank()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = item.exampleMeaning,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (itemWithProgress.correctCount > 0 || itemWithProgress.wrongCount > 0) {
                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Practice history · ${itemWithProgress.correctCount} correct · ${itemWithProgress.wrongCount} missed",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(20.dp))

            // Toggle Mastery Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (isMastered) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            Color.White
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = if (isMastered) Color.White.copy(alpha = 0.10f) else Color.Transparent,
                        shape = CircleShape,
                    )
                    .clickable(onClick = onToggleMastery),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (isMastered) "Mark as Needs Practice" else "✓ Mark as Mastered",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isMastered) Color.White else Color.Black,
                )
            }
        }
    }
}
