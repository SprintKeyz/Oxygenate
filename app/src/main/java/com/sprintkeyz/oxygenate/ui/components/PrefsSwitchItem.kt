package com.sprintkeyz.oxygenate.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.sprintkeyz.oxygenate.data.ConfigItem
import com.sprintkeyz.oxygenate.data.rememberPref
import com.sprintkeyz.oxygenate.data.setPref
import com.sprintkeyz.oxygenate.ui.components.items.SwitchItem

@Composable
fun PrefsSwitchItem(
    configItem: ConfigItem<Boolean>,
    icon: ImageVector,
    title: String,
    subtitle: String,
    disabled: Boolean = false
) {
    val ctx = LocalContext.current

    @Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
    var currentValue by rememberPref(configItem)

    SwitchItem(
        icon = icon,
        title = title,
        subtitle = subtitle,
        checked = currentValue,
        disabled = disabled,
        onCheckedChange = { newValue ->
            currentValue = newValue
            setPref(ctx, configItem, currentValue)
        }
    )
}