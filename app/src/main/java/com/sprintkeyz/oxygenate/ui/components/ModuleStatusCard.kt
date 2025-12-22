package com.sprintkeyz.oxygenate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ModuleStatusCardItem(
    isActive: Boolean,
    isRootAvailable: Boolean
) {
    var containerColor = MaterialTheme.colorScheme.errorContainer
    var textColor = MaterialTheme.colorScheme.onErrorContainer
    var title = "Disabled"
    var description = "Please activate Oxygenate in LSPosed"

    // fully active if both root and xposed are available
    if (isActive && isRootAvailable) {
        containerColor = MaterialTheme.colorScheme.primaryContainer
        textColor = MaterialTheme.colorScheme.onPrimaryContainer
        title = "Enabled"
        description = "Oxygenate is active"
    }

    // no root enabled
    else if (isActive) {
        containerColor = Color.hsv(47F, 0.87F, 1.0F)
        textColor = Color.hsv(42F, 1.0F, 0.23F)
        title = "Partially Enabled"
        description = "Oxygenate is enabled, but requires root to fully work"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}
