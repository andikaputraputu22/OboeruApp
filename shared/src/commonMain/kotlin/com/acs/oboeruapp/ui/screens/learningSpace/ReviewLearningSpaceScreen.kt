package com.acs.oboeruapp.ui.screens.learningSpace

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.ui.components.AddBlockRow
import com.acs.oboeruapp.ui.components.ReviewBlockCard
import com.acs.oboeruapp.ui.components.ReviewLearningSpaceCard
import com.acs.oboeruapp.ui.components.ReviewLearningSpaceCoverImage
import com.acs.oboeruapp.ui.components.RoundedButton
import com.acs.oboeruapp.utils.dummyDeckBlock
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_back
import oboeruapp.shared.generated.resources.torii_gate

@Composable
fun ReviewLearningSpaceScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            item {
                Text(
                    text = "Review\nYour Learning Space",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(24.dp))
            }

            item {
                ReviewLearningSpaceCoverImage(
                    backgroundImageUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?q=80&w=1470&auto=format&fit=crop"
                )
                Spacer(Modifier.height(8.dp))
            }

            item {
                ReviewLearningSpaceCard(
                    icon = Res.drawable.torii_gate,
                    title = "JLPT N2 Preparation",
                    description = "My journey to pass JLPT N2!"
                )
                Spacer(Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Included Blocks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    dummyDeckBlock.forEachIndexed { index, block ->
                        ReviewBlockCard(
                            block = block,
                            onMenuClick = {}
                        )
                        if (index != dummyDeckBlock.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
                AddBlockRow(onClick = { /* TODO */ })
            }
        }

        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Create Learning Space", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}