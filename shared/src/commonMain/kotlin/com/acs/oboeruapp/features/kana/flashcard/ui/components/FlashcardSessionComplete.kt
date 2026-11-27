package com.acs.oboeruapp.features.kana.flashcard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_check
import org.jetbrains.compose.resources.vectorResource

@Composable
fun FlashcardSessionComplete(
    title: String = "Session Complete!",
    subtitle: String,
    goodCount: Int,
    easyCount: Int,
    againCount: Int,
    accuracyPercent: Int,
    primaryButtonText: String = "Practice Again",
    onPrimaryClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val emerald = Color(0xFF4ADE80)
    val cardShape = RoundedCornerShape(24.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF162922), Color(0xFF18191F))
                    )
                )
                .border(1.dp, emerald.copy(alpha = 0.28f), cardShape)
                .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(emerald.copy(alpha = 0.16f))
                    .border(1.dp, emerald.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_check),
                    contentDescription = null,
                    tint = emerald,
                    modifier = Modifier.size(26.dp),
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(emerald.copy(alpha = 0.14f))
                    .border(1.dp, emerald.copy(alpha = 0.28f), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 5.dp),
            ) {
                Text(
                    text = "$accuracyPercent% recall rate",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = emerald,
                )
            }

            Spacer(Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SessionStatTile(
                    label = "Good",
                    value = goodCount.toString(),
                    accentColor = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f),
                )
                SessionStatTile(
                    label = "Easy",
                    value = easyCount.toString(),
                    accentColor = emerald,
                    modifier = Modifier.weight(1f),
                )
                SessionStatTile(
                    label = "Again",
                    value = againCount.toString(),
                    accentColor = Color(0xFFF87171),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = onPrimaryClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = primaryButtonText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
        }

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                .clickable(onClick = onDoneClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Back to Home",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun SessionStatTile(
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(Color.White.copy(alpha = 0.04f))
            .border(1.dp, Color.White.copy(alpha = 0.06f), shape)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
