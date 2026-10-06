package com.acs.oboeruapp.ui.screens.learningSpace

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.ui.components.ColorOptionItem
import com.acs.oboeruapp.ui.components.IconOptionItem
import com.acs.oboeruapp.ui.components.RoundedButton
import com.acs.oboeruapp.utils.dashedBorder
import com.acs.oboeruapp.utils.dummyColorOptions
import com.acs.oboeruapp.utils.dummyIconOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import kotlinx.coroutines.launch
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_add_photo
import oboeruapp.shared.generated.resources.ic_back
import org.jetbrains.compose.resources.vectorResource

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
    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }

    val descriptionMaxLength = 100

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
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
                        text = "Cover Image",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(8.dp))

                    val scope = rememberCoroutineScope()
                    val imagePicker = rememberImagePickerLauncher(
                        selectionMode = SelectionMode.Single,
                        scope = scope,
                        onResult = { byteArrays ->
                            byteArrays.firstOrNull()?.let { bytes ->
                                selectedImageBytes = bytes
                            }
                        },
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(16.dp))
                            .dashedBorder(
                                color = MaterialTheme.colorScheme.outline,
                                cornerRadius = 16.dp,
                                strokeWidth = 1.5.dp,
                                dashLength = 6.dp,
                                gapLength = 4.dp,
                            )
                            .clickable { imagePicker.launch() },
                        contentAlignment = Alignment.Center,
                    ) {
                        val imageBytes = selectedImageBytes
                        if (imageBytes != null) {
                            Image(
                                bitmap = remember(imageBytes) {
                                    imageBytes.toImageBitmap()
                                },
                                contentDescription = "Selected photo",
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.ic_add_photo),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Add cover image",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "Space Name",
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
                        text = "Icon",
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
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
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
                    if (name.isBlank()) {
                        scope.launch {
                            snackBarHostState.showSnackbar("Please give your Learning Space a name")
                        }
                    } else {
                        onNextClick(name, selectedIconId, selectedColorId, description)
                    }
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
}