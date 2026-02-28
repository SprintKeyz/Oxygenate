package com.sprintkeyz.oxygenate.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlin.time.DurationUnit

@Composable
inline fun <reified T> rememberPref(
    configItem: ConfigItem<T>,
    durationUnit: DurationUnit? = null,
    percentage: Boolean = false
): MutableState<T> {
    val ctx = LocalContext.current

    return remember {
        mutableStateOf(getPref(ctx, configItem, durationUnit, percentage))
    }
}

