package com.acs.oboeruapp.features.learningspace.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.features.learningspace.ui.model.confettiDots
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_check
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SuccessCheckmark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(220.dp),
        contentAlignment = Alignment.Center,
    ) {
        confettiDots.forEachIndexed { index, dot ->
            AnimatedConfettiDot(
                dot = dot,
                delayMillis = index * 60,
            )
        }

        var checkmarkVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            checkmarkVisible = true
        }
        val checkmarkScale by animateFloatAsState(
            targetValue = if (checkmarkVisible) 1f else 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = checkmarkScale
                    scaleY = checkmarkScale
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_check),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp),
            )
        }
    }
}

