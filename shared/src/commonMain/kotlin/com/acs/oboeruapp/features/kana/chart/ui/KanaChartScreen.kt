package com.acs.oboeruapp.features.kana.chart.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acs.oboeruapp.core.designsystem.components.OboeruSegmentedSwitcher
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.features.kana.chart.KanaChartGridBuilder
import com.acs.oboeruapp.features.kana.chart.KanaChartRow
import com.acs.oboeruapp.features.kana.chart.KanaChartViewModel
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItem
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.profile.ui.components.ProfileTopBar
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun KanaChartScreen(
    initialScript: KanaScript = KanaScript.HIRAGANA,
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onBackClick: () -> Unit = {},
    viewModel: KanaChartViewModel = koinViewModel(
        key = "kana_chart_${initialScript.name}",
        parameters = { parametersOf(initialScript) },
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    KanaChartScreenContent(
        uiState = uiState,
        topBarPadding = topBarPadding,
        bottomBarPadding = bottomBarPadding,
        onBackClick = onBackClick,
        onScriptSelected = viewModel::onScriptSelected,
        onCategorySelected = viewModel::onCategorySelected,
        onCharacterClick = viewModel::onCharacterClick,
    )

    uiState.selectedItem?.let { selected ->
        KanaDetailBottomSheet(
            itemWithProgress = selected,
            onDismiss = viewModel::onDismissDetail,
            onToggleMastery = { viewModel.onToggleMastery(selected) },
        )
    }
}

@Composable
private fun KanaChartScreenContent(
    uiState: KanaChartUiState,
    topBarPadding: Dp = 0.dp,
    bottomBarPadding: Dp = 0.dp,
    onBackClick: () -> Unit = {},
    onScriptSelected: (KanaScript) -> Unit = {},
    onCategorySelected: (KanaCategory) -> Unit = {},
    onCharacterClick: (KanaItemWithProgress) -> Unit = {},
) {
    val effectiveTopPadding = if (topBarPadding > 0.dp) {
        topBarPadding
    } else {
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    }

    val effectiveBottomPadding = if (bottomBarPadding > 0.dp) {
        bottomBarPadding
    } else {
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    }

    val scriptAccent = if (uiState.selectedScript == KanaScript.HIRAGANA) {
        Color(0xFF38BDF8)
    } else {
        Color(0xFFA78BFA)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = effectiveTopPadding + 8.dp,
            end = 16.dp,
            bottom = effectiveBottomPadding + 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item(key = "top_bar") {
            ProfileTopBar(
                title = "Character Table",
                onBackClick = onBackClick,
            )
        }

        item(key = "script_switcher") {
            val scriptOptions = remember {
                listOf(
                    KanaScript.HIRAGANA to "Hiragana  あ",
                    KanaScript.KATAKANA to "Katakana  ア",
                )
            }
            OboeruSegmentedSwitcher(
                options = scriptOptions,
                selectedOption = uiState.selectedScript,
                onOptionSelected = onScriptSelected,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(top = 6.dp),
            )
        }

        if (!uiState.isLoading) {
            item(key = "mastery_banner") {
                KanaMasteryBanner(
                    script = uiState.selectedScript,
                    masteredCount = uiState.masteredCount,
                    learningCount = uiState.learningCount,
                    totalCount = uiState.totalCount,
                    progressFraction = uiState.masteryFraction,
                    accentColor = scriptAccent,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        item(key = "category_tabs") {
            CategoryChipRow(
                selectedCategory = uiState.selectedCategory,
                accentColor = scriptAccent,
                onSelectCategory = onCategorySelected,
                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
            )
        }

        if (uiState.rows.isNotEmpty()) {
            item(key = "column_headers_${uiState.selectedCategory.name}") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    uiState.columnHeaders.forEach { header ->
                        Text(
                            text = header,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            items(
                items = uiState.rows,
                key = { row -> "${uiState.selectedScript.name}_${uiState.selectedCategory.name}_${row.rowIndex}" },
            ) { row ->
                KanaGridRow(
                    row = row,
                    onCharacterClick = onCharacterClick,
                )
            }
        }
    }
}

@Composable
private fun KanaMasteryBanner(
    script: KanaScript,
    masteredCount: Int,
    learningCount: Int,
    totalCount: Int,
    progressFraction: Float,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    val percent = (progressFraction * 100f).toInt()
    val allMastered = totalCount > 0 && masteredCount >= totalCount
    val activeColor = if (allMastered) Color(0xFF4ADE80) else accentColor
    val gradientStart = when {
        allMastered -> Color(0xFF162922)
        script == KanaScript.HIRAGANA -> Color(0xFF152432)
        else -> Color(0xFF211A30)
    }
    val gradientEnd = Color(0xFF18191F)
    val shape = RoundedCornerShape(22.dp)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.coerceIn(0f, 1f),
        label = "kana_chart_mastery_progress",
    )

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
                color = activeColor.copy(alpha = 0.22f),
                shape = shape,
            )
            .padding(horizontal = 18.dp, vertical = 15.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (script == KanaScript.HIRAGANA) "HIRAGANA MASTERY" else "KATAKANA MASTERY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = activeColor.copy(alpha = 0.9f),
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$masteredCount",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 26.sp,
                    )
                    Text(
                        text = " / $totalCount",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "mastered",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(activeColor.copy(alpha = 0.14f))
                        .border(
                            width = 1.dp,
                            color = activeColor.copy(alpha = 0.28f),
                            shape = CircleShape,
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "$percent%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = activeColor,
                    )
                }

                if (learningCount > 0) {
                    Spacer(Modifier.height(6.dp))
                    LegendDot(color = Color(0xFFF5C842), label = "$learningCount learning")
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f)),
        ) {
            if (animatedProgress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(activeColor),
                )
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CategoryChipRow(
    selectedCategory: KanaCategory,
    accentColor: Color,
    onSelectCategory: (KanaCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    val categories = listOf(
        KanaCategory.BASIC to "Basic · 46",
        KanaCategory.DAKUTEN to "Dakuten · 25",
        KanaCategory.YOON to "Yoon · 33",
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.forEach { (category, label) ->
            val isSelected = category == selectedCategory
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            accentColor.copy(alpha = 0.16f)
                        } else {
                            Color.White.copy(alpha = 0.05f)
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) {
                            accentColor.copy(alpha = 0.45f)
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape,
                    )
                    .clickable { onSelectCategory(category) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun KanaGridRow(
    row: KanaChartRow,
    onCharacterClick: (KanaItemWithProgress) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        row.cells.forEach { cell ->
            if (cell == null) {
                Spacer(modifier = Modifier.weight(1f))
            } else {
                KanaGridCell(
                    itemWithProgress = cell,
                    onClick = { onCharacterClick(cell) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun KanaGridCell(
    itemWithProgress: KanaItemWithProgress,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val item = itemWithProgress.item
    val shape = RoundedCornerShape(16.dp)

    val (bgColor, borderColor, dotColor) = when (itemWithProgress.status) {
        ProgressStatus.MASTERED -> Triple(
            Color(0xFF4ADE80).copy(alpha = 0.10f),
            Color(0xFF4ADE80).copy(alpha = 0.42f),
            Color(0xFF4ADE80),
        )

        ProgressStatus.LEARNING -> Triple(
            Color(0xFFF5C842).copy(alpha = 0.08f),
            Color(0xFFF5C842).copy(alpha = 0.35f),
            Color(0xFFF5C842),
        )

        ProgressStatus.NEW -> Triple(
            MaterialTheme.colorScheme.surface,
            Color.White.copy(alpha = 0.07f),
            null,
        )
    }

    Box(
        modifier = modifier
            .height(70.dp)
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(6.dp),
    ) {
        if (dotColor != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 2.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = item.character,
                fontSize = if (item.character.length > 1) 20.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = item.romaji,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun KanaChartScreenPreview() {
    val sampleItems = listOf(
        KanaItemWithProgress(
            item = KanaItem("hiragana_a", KanaScript.HIRAGANA, KanaCategory.BASIC, "a", "あ", "a", "あめ (ame)", "Rain", 0, 0, 0),
            status = ProgressStatus.MASTERED,
        ),
        KanaItemWithProgress(
            item = KanaItem("hiragana_i", KanaScript.HIRAGANA, KanaCategory.BASIC, "a", "い", "i", "いぬ (inu)", "Dog", 0, 1, 1),
            status = ProgressStatus.LEARNING,
        ),
        KanaItemWithProgress(
            item = KanaItem("hiragana_u", KanaScript.HIRAGANA, KanaCategory.BASIC, "a", "う", "u", "うみ (umi)", "Sea", 0, 2, 2),
            status = ProgressStatus.NEW,
        ),
        KanaItemWithProgress(
            item = KanaItem("hiragana_e", KanaScript.HIRAGANA, KanaCategory.BASIC, "a", "え", "e", "えき (eki)", "Station", 0, 3, 3),
            status = ProgressStatus.NEW,
        ),
        KanaItemWithProgress(
            item = KanaItem("hiragana_o", KanaScript.HIRAGANA, KanaCategory.BASIC, "a", "お", "o", "おちゃ (ocha)", "Tea", 0, 4, 4),
            status = ProgressStatus.MASTERED,
        ),
    )

    OboeruTheme {
        KanaChartScreenContent(
            uiState = KanaChartUiState(
                isLoading = false,
                selectedScript = KanaScript.HIRAGANA,
                selectedCategory = KanaCategory.BASIC,
                columnHeaders = KanaChartGridBuilder.columnHeadersFor(KanaCategory.BASIC),
                rows = KanaChartGridBuilder.buildRows(sampleItems, KanaCategory.BASIC),
                masteredCount = 2,
                learningCount = 1,
                totalCount = 104,
            ),
        )
    }
}
