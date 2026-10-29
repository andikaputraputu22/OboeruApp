package com.acs.oboeruapp.features.learningspace.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.core.designsystem.components.RoundedButton
import com.acs.oboeruapp.features.learningspace.ui.components.CreateFromScratchCard
import com.acs.oboeruapp.features.learningspace.ui.components.LearningSpaceTemplateCard
import com.acs.oboeruapp.utils.dummyLearningSpaceTemplate
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_back

@Composable
fun ChooseTemplateScreen(
    onBackClick: () -> Unit,
    onSkipClick: (() -> Unit)? = null,
    onNextClick: (selectedTemplateId: String?) -> Unit,
    onCreateFromScratchClick: () -> Unit,
) {
    var selectedId by remember { mutableStateOf(dummyLearningSpaceTemplate.firstOrNull()?.id) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoundedButton(
                onClick = onBackClick,
                icon = Res.drawable.ic_back
            )
            if (onSkipClick != null) {
                TextButton(onClick = onSkipClick) {
                    Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        ) {
            item {
                Text(
                    text = "Choose a Template",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Start with a ready-made Learning Space or create your own.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(20.dp))
            }

            items(
                items = dummyLearningSpaceTemplate,
                key = { it.id },
            ) { template ->
                LearningSpaceTemplateCard(
                    template = template,
                    selected = template.id == selectedId,
                    onClick = { selectedId = template.id },
                )
                Spacer(Modifier.height(12.dp))
            }

            item {
                CreateFromScratchCard(onClick = onCreateFromScratchClick)
                Spacer(Modifier.height(16.dp))
            }
        }

        Button(
            onClick = { onNextClick(selectedId) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Next", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

