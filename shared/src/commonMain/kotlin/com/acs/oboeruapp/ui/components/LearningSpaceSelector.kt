package com.acs.oboeruapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.data.model.LearningSpace

@Composable
fun LearningSpaceSelector(
    spaces: List<LearningSpace>,
    selectedId: String,
    onSpaceClick: (LearningSpace) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = spaces,
                key = { it.id },
            ) { space ->
                LearningSpaceChip(
                    space = space,
                    selected = space.id == selectedId,
                    onClick = { onSpaceClick(space) },
                )
            }
            item {
                // spacer kecil biar chip terakhir tidak nempel ke tombol
                Spacer(Modifier.width(4.dp))
            }
        }

        Spacer(Modifier.width(8.dp))

        AddLearningSpace(
            onClick = onAddClick,
            modifier = Modifier
        )
    }
}