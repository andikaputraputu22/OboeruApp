package com.acs.oboeruapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_notification
import org.jetbrains.compose.resources.vectorResource

@Composable
fun UserHeader(
    isAuthenticated: Boolean,
    photoUrl: String?,
    displayName: String?,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(
                photoUrl = photoUrl,
                onClick = onProfileClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.height(42.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = if (isAuthenticated) "Konbanwa," else "Welcome to,",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (isAuthenticated) displayName ?: "" else "Oboeru",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        IconButton(
            onClick = onNotificationClick
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_notification),
                contentDescription = "Notifications",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}