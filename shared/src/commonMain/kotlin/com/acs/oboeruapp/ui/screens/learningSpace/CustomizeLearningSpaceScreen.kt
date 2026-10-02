package com.acs.oboeruapp.ui.screens.learningSpace

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.ui.components.ColorOptionItem
import com.acs.oboeruapp.ui.components.IconOptionItem
import com.acs.oboeruapp.ui.components.RoundedButton
import com.acs.oboeruapp.utils.dummyColorOptions
import com.acs.oboeruapp.utils.dummyIconOptions
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_back

@Composable
fun CustomizeLearningSpaceScreen(
    initialName: String = "",
    initialDescription: String = "",
    onBackClick: () -> Unit,
    onNextClick: (name: String, coverId: String, colorId: String, description: String) -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var selectedIconId by remember { mutableStateOf(dummyIconOptions.first().id) }
    var selectedColorId by remember { mutableStateOf(dummyColorOptions.first().id) }

    val descriptionMaxLength = 100

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        RoundedButton(
            onClick = onBackClick,
            icon = Res.drawable.ic_back,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        ) {
            item {
                Text(
                    text = "Customize\nYour Learning Space",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "e.g. JLPT Preparation",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                Spacer(Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Cover",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    dummyIconOptions.forEach { icon ->
                        IconOptionItem(
                            icon = icon,
                            selected = icon.id == selectedIconId,
                            onClick = { selectedIconId = icon.id },
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Color",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    dummyColorOptions.forEach { colorOption ->
                        ColorOptionItem(
                            colorOption = colorOption,
                            selected = colorOption.id == selectedColorId,
                            onClick = { selectedColorId = colorOption.id },
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Description (optional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { if (it.length <= descriptionMaxLength) description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "Add a short description for your Learning Space...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${description.length}/$descriptionMaxLength",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        Button(
            onClick = {
                onNextClick(name, selectedIconId, selectedColorId, description)
            },
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