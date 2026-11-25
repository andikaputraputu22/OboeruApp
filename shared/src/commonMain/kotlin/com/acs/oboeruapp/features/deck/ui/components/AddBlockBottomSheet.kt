package com.acs.oboeruapp.features.deck.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acs.oboeruapp.features.deck.ActiveBlockKey
import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.DeckCatalog
import com.acs.oboeruapp.features.deck.domain.model.DeckTopic
import com.acs.oboeruapp.features.deck.domain.model.StarterPack
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_arrow_right
import oboeruapp.shared.generated.resources.ic_back
import oboeruapp.shared.generated.resources.ic_check
import oboeruapp.shared.generated.resources.ic_close
import oboeruapp.shared.generated.resources.ic_timer
import org.jetbrains.compose.resources.vectorResource

private enum class AddBlockTab(val label: String) {
    TOPICS("Topics"),
    PACKS("Quick Packs"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBlockBottomSheet(
    activeBlockKeys: Set<ActiveBlockKey>,
    onDismiss: () -> Unit,
    onAddBlock: (topic: DeckTopic, activityType: DeckActivityType, dailyTarget: Int) -> Unit,
    onAddStarterPack: (StarterPack) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedTab by remember { mutableStateOf(AddBlockTab.TOPICS) }
    var selectedTopic by remember { mutableStateOf<DeckTopic?>(null) }

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
        ) {
            // Top Header (adapts cleanly between Step 1 & Step 2)
            SheetHeader(
                selectedTopic = selectedTopic,
                onBackClick = { selectedTopic = null },
                onCloseClick = onDismiss,
            )

            Spacer(Modifier.height(18.dp))

            AnimatedContent(
                targetState = selectedTopic,
                transitionSpec = {
                    val forward = targetState != null
                    if (forward) {
                        (slideInHorizontally(tween(220)) { it / 4 } + fadeIn(tween(200)))
                            .togetherWith(slideOutHorizontally(tween(220)) { -it / 4 } + fadeOut(tween(150)))
                    } else {
                        (slideInHorizontally(tween(220)) { -it / 4 } + fadeIn(tween(200)))
                            .togetherWith(slideOutHorizontally(tween(220)) { it / 4 } + fadeOut(tween(150)))
                    }
                },
            ) { topic ->
                if (topic == null) {
                    StepChooseTopicOrPack(
                        selectedTab = selectedTab,
                        activeBlockKeys = activeBlockKeys,
                        onSelectTab = { selectedTab = it },
                        onTopicSelected = { selectedTopic = it },
                        onQuickAddReview = {
                            onAddBlock(DeckTopic.ALL_REVIEW, DeckActivityType.REVIEW, 0)
                        },
                        onAddStarterPack = onAddStarterPack,
                    )
                } else {
                    StepConfigureBlock(
                        topic = topic,
                        activeBlockKeys = activeBlockKeys,
                        onAddBlock = { activity, target ->
                            onAddBlock(topic, activity, target)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SheetHeader(
    selectedTopic: DeckTopic?,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (selectedTopic != null) {
            SheetIconCircleButton(
                icon = Res.drawable.ic_back,
                contentDescription = "Back",
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart),
            )
        }

        Text(
            text = selectedTopic?.title ?: "Add Study Block",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 48.dp),
        )

        SheetIconCircleButton(
            icon = Res.drawable.ic_close,
            contentDescription = "Close",
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
internal fun SheetIconCircleButton(
    icon: org.jetbrains.compose.resources.DrawableResource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = vectorResource(icon),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun StepChooseTopicOrPack(
    selectedTab: AddBlockTab,
    activeBlockKeys: Set<ActiveBlockKey>,
    onSelectTab: (AddBlockTab) -> Unit,
    onTopicSelected: (DeckTopic) -> Unit,
    onQuickAddReview: () -> Unit,
    onAddStarterPack: (StarterPack) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Segmented Pill Switcher ("Topics" | "Quick Packs")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                .border(1.dp, Color.White.copy(alpha = 0.07f), CircleShape)
                .padding(4.dp),
        ) {
            AddBlockTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color.White.copy(alpha = 0.14f) else Color.Transparent
                        )
                        .clickable { onSelectTab(tab) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = tab.label,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) {
                            Color.White
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        when (selectedTab) {
            AddBlockTab.TOPICS -> {
                SectionLabel("KANA")
                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TopicEntryRow(
                        glyph = "あ",
                        title = DeckTopic.HIRAGANA.title,
                        accentColor = Color(0xFF38BDF8),
                        addedCount = DeckCatalog.activitiesForTopic(DeckTopic.HIRAGANA).count {
                            ActiveBlockKey(DeckTopic.HIRAGANA, it) in activeBlockKeys
                        },
                        totalCount = DeckCatalog.activitiesForTopic(DeckTopic.HIRAGANA).size,
                        onClick = { onTopicSelected(DeckTopic.HIRAGANA) },
                    )

                    TopicEntryRow(
                        glyph = "ア",
                        title = DeckTopic.KATAKANA.title,
                        accentColor = Color(0xFFA78BFA),
                        addedCount = DeckCatalog.activitiesForTopic(DeckTopic.KATAKANA).count {
                            ActiveBlockKey(DeckTopic.KATAKANA, it) in activeBlockKeys
                        },
                        totalCount = DeckCatalog.activitiesForTopic(DeckTopic.KATAKANA).size,
                        onClick = { onTopicSelected(DeckTopic.KATAKANA) },
                    )
                }

                Spacer(Modifier.height(20.dp))
                SectionLabel("SPACED REPETITION")
                Spacer(Modifier.height(8.dp))

                val isReviewAdded =
                    ActiveBlockKey(DeckTopic.ALL_REVIEW, DeckActivityType.REVIEW) in activeBlockKeys
                QuickReviewRow(
                    isAdded = isReviewAdded,
                    onClick = { if (!isReviewAdded) onQuickAddReview() },
                )

                Spacer(Modifier.height(20.dp))
                SectionLabel("JLPT LEVELS")
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "JLPT N5 – N1",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "Vocabulary, Kanji & Grammar",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.06f))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = "Coming soon",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            AddBlockTab.PACKS -> {
                var selectedPack by remember { mutableStateOf(DeckCatalog.starterPacks.first()) }
                var hasUserPickedPack by remember { mutableStateOf(false) }

                LaunchedEffect(activeBlockKeys) {
                    if (!hasUserPickedPack) {
                        val firstUnadded = DeckCatalog.starterPacks.firstOrNull { pack ->
                            pack.blocks.any { spec ->
                                ActiveBlockKey(spec.topic, spec.activityType) !in activeBlockKeys
                            }
                        } ?: DeckCatalog.starterPacks.first()
                        selectedPack = firstUnadded
                    }
                }

                val isSelectedPackFullyAdded = selectedPack.blocks.all { spec ->
                    ActiveBlockKey(spec.topic, spec.activityType) in activeBlockKeys
                }

                SectionLabel("CHOOSE A PACK")
                Spacer(Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DeckCatalog.starterPacks.forEach { pack ->
                        val allPackBlocksAdded = pack.blocks.all { spec ->
                            ActiveBlockKey(spec.topic, spec.activityType) in activeBlockKeys
                        }
                        val isSelected = pack.id == selectedPack.id
                        val accent = getPackAccentColor(pack.id)

                        StarterPackRow(
                            pack = pack,
                            accentColor = accent,
                            isSelected = isSelected,
                            isFullyAdded = allPackBlocksAdded,
                            onClick = {
                                hasUserPickedPack = true
                                selectedPack = pack
                            },
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelectedPackFullyAdded) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                Color.White
                            }
                        )
                        .clickable(enabled = !isSelectedPackFullyAdded) {
                            onAddStarterPack(selectedPack)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (isSelectedPackFullyAdded) "Already on Home" else "Add to Home",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelectedPackFullyAdded) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            Color.Black
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun StepConfigureBlock(
    topic: DeckTopic,
    activeBlockKeys: Set<ActiveBlockKey>,
    onAddBlock: (activityType: DeckActivityType, dailyTarget: Int) -> Unit,
) {
    val availableActivities = remember(topic) { DeckCatalog.activitiesForTopic(topic) }
    var selectedActivity by remember(topic) { mutableStateOf(availableActivities.first()) }
    var hasUserPickedActivity by remember(topic) { mutableStateOf(false) }

    LaunchedEffect(topic, activeBlockKeys) {
        if (!hasUserPickedActivity) {
            selectedActivity = availableActivities.firstOrNull {
                ActiveBlockKey(topic, it) !in activeBlockKeys
            } ?: availableActivities.first()
        }
    }

    var selectedTarget by remember(selectedActivity) {
        mutableIntStateOf(selectedActivity.defaultDailyTarget.coerceAtLeast(5))
    }

    val isAlreadyAdded = ActiveBlockKey(topic, selectedActivity) in activeBlockKeys
    val showsTargetPicker = selectedActivity == DeckActivityType.FLASHCARD ||
        selectedActivity == DeckActivityType.QUIZ

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionLabel("ACTIVITY MODE")
        Spacer(Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            availableActivities.forEach { activity ->
                val isSelected = activity == selectedActivity
                val alreadyPinned = ActiveBlockKey(topic, activity) in activeBlockKeys
                val accent = getBlockAccentColor(topic, activity)

                ActivityModeOptionRow(
                    activity = activity,
                    accentColor = accent,
                    isSelected = isSelected,
                    isAlreadyPinned = alreadyPinned,
                    onClick = {
                        hasUserPickedActivity = true
                        selectedActivity = activity
                    },
                )
            }
        }

        if (showsTargetPicker) {
            Spacer(Modifier.height(22.dp))
            SectionLabel("DAILY GOAL (ITEMS PER DAY)")
            Spacer(Modifier.height(10.dp))

            DailyGoalPresetRow(
                selectedTarget = selectedTarget,
                accentColor = getBlockAccentColor(topic, selectedActivity),
                onSelectTarget = { selectedTarget = it },
            )
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(CircleShape)
                .background(
                    if (isAlreadyAdded) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        Color.White
                    }
                )
                .clickable(enabled = !isAlreadyAdded) {
                    val target = if (showsTargetPicker) selectedTarget else 0
                    onAddBlock(selectedActivity, target)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (isAlreadyAdded) "Already on Home" else "Add to Home",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isAlreadyAdded) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    Color.Black
                },
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        modifier = Modifier.padding(start = 4.dp),
    )
}

@Composable
private fun TopicEntryRow(
    glyph: String,
    title: String,
    accentColor: Color,
    addedCount: Int,
    totalCount: Int,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
            .border(1.dp, Color.White.copy(alpha = 0.06f), shape)
            .clickable(onClick = onClick)
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
            Text(
                text = glyph,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "$addedCount of $totalCount blocks added",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Icon(
            imageVector = vectorResource(Res.drawable.ic_arrow_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun QuickReviewRow(
    isAdded: Boolean,
    onClick: () -> Unit,
) {
    val accent = Color(0xFFFB923C)
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
            .border(1.dp, Color.White.copy(alpha = 0.06f), shape)
            .clickable(enabled = !isAdded, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_timer),
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Today's Review",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "All scheduled SRS items due today",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (isAdded) {
                        Color(0xFF4ADE80).copy(alpha = 0.15f)
                    } else {
                        accent.copy(alpha = 0.16f)
                    }
                )
                .padding(horizontal = 12.dp, vertical = 5.dp),
        ) {
            Text(
                text = if (isAdded) "✓ Added" else "+ Add",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isAdded) Color(0xFF4ADE80) else accent,
            )
        }
    }
}

private fun getPackAccentColor(packId: String): Color =
    when (packId) {
        "pack_hiragana" -> Color(0xFF38BDF8)
        "pack_katakana" -> Color(0xFFA78BFA)
        "pack_kana_review" -> Color(0xFFFB923C)
        else -> Color(0xFF7AA2E3)
    }

@Composable
private fun StarterPackRow(
    pack: StarterPack,
    accentColor: Color,
    isSelected: Boolean,
    isFullyAdded: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                if (isSelected) {
                    accentColor.copy(alpha = 0.12f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                }
            )
            .border(
                width = 1.dp,
                color = if (isSelected) {
                    accentColor.copy(alpha = 0.55f)
                } else {
                    Color.White.copy(alpha = 0.06f)
                },
                shape = shape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pack.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${pack.blocks.size} blocks · ${pack.subtitle}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (isFullyAdded) {
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF4ADE80).copy(alpha = 0.14f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = "Added",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4ADE80),
                )
            }
        } else if (isSelected) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = vectorResource(Res.drawable.ic_check),
                contentDescription = "Selected",
                tint = accentColor,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun ActivityModeOptionRow(
    activity: DeckActivityType,
    accentColor: Color,
    isSelected: Boolean,
    isAlreadyPinned: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)
    val description = when (activity) {
        DeckActivityType.CHART -> "Interactive character reference table"
        DeckActivityType.FLASHCARD -> "Flip cards with spaced repetition"
        DeckActivityType.QUIZ -> "Multiple-choice recognition practice"
        DeckActivityType.REVIEW -> "All scheduled SRS items due today"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                if (isSelected) {
                    accentColor.copy(alpha = 0.12f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                }
            )
            .border(
                width = 1.dp,
                color = if (isSelected) {
                    accentColor.copy(alpha = 0.55f)
                } else {
                    Color.White.copy(alpha = 0.06f)
                },
                shape = shape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
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
                imageVector = vectorResource(getBlockIcon(activity)),
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.label,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (isAlreadyPinned) {
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF4ADE80).copy(alpha = 0.14f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = "Added",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4ADE80),
                )
            }
        } else if (isSelected) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = vectorResource(Res.drawable.ic_check),
                contentDescription = "Selected",
                tint = accentColor,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
internal fun DailyGoalPresetRow(
    selectedTarget: Int,
    accentColor: Color,
    onSelectTarget: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DeckCatalog.dailyTargetPresets.forEach { preset ->
            val isSelected = selectedTarget == preset
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) {
                            accentColor.copy(alpha = 0.18f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) {
                            accentColor.copy(alpha = 0.6f)
                        } else {
                            Color.White.copy(alpha = 0.06f)
                        },
                        shape = RoundedCornerShape(14.dp),
                    )
                    .clickable { onSelectTarget(preset) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "$preset",
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
