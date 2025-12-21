package com.sprintkeyz.oxygenate.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.factory.prefs
import com.sprintkeyz.oxygenate.data.DataConst
import com.sprintkeyz.oxygenate.ui.theme.OxygenateTheme
import com.sprintkeyz.oxygenate.utils.isRootAvailable
import com.sprintkeyz.oxygenate.utils.restartSystemUI

// two status bar tweak ideas:
// red 1 always visible
// disable red 1

// disable red 1 is the top setting, if that is enabled then red 1 always visible is disabled and greyed out

class ConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // display a toast if root
        if (isRootAvailable()) {
            Toast.makeText(this, "Root available", Toast.LENGTH_SHORT).show()
        }

        else {
            Toast.makeText(this, "Root NOT available", Toast.LENGTH_SHORT).show()
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OxygenateTheme {
                ConfigScreen(
                    isModuleActive = YukiHookAPI.Status.isModuleActive,
                    isRooted = isRootAvailable(),
                    context = this
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    isModuleActive: Boolean,
    isRooted: Boolean,
    context: Context
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    // get red one status
    var isRedOneVisible by remember {
        mutableStateOf(context.prefs().get(DataConst.HOOK_RED_ONE_ALWAYS_VISIBLE))
    }

    fun onRedOneVisibleChange(enabled: Boolean) {
        context.prefs().edit {
            put(DataConst.HOOK_RED_ONE_ALWAYS_VISIBLE, enabled)
            isRedOneVisible = enabled
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Preferences",
                        fontSize = androidx.compose.ui.unit.lerp(
                            34.sp,
                            22.sp,
                            scrollBehavior.state.collapsedFraction
                        )
                    )

                },
                scrollBehavior = scrollBehavior,
                actions = {
                    // Restart SystemUI button
                    IconButton(onClick = {
                        restartSystemUI()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restart SystemUI",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            SectionHeaderItem("Status")
            Spacer(modifier = Modifier.height(8.dp))
            
            ModuleStatusCardItem(
                isActive = isModuleActive,
                isRootAvailable = isRooted
            )

            Spacer(modifier = Modifier.height(8.dp))
            
            SectionHeaderItem("Appearance")
            Spacer(modifier = Modifier.height(8.dp))

            SubmenuItem(
                icon = Icons.Default.SignalCellularAlt,
                title = "Status Bar",
                subtitle = "Status bar appearance tweaks",
                onClick = { }
            )

            Spacer(modifier = Modifier.height(8.dp))


            // temporarily here
            SwitchItem(
                icon = Icons.Default.AccessTime,
                title = "Red 1 Always Visible",
                subtitle = "Makes the clock red 1 always visible",
                checked = isRedOneVisible,
                onCheckedChange = ::onRedOneVisibleChange
            )
        }
    }
}

@Composable
fun SectionHeaderItem(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

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

@Composable
fun SubmenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}