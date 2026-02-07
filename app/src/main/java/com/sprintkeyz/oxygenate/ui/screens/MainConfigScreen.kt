package com.sprintkeyz.oxygenate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sprintkeyz.oxygenate.ui.components.ModuleStatusCardItem
import com.sprintkeyz.oxygenate.ui.components.SectionHeaderItem
import com.sprintkeyz.oxygenate.ui.components.SubmenuItem
import com.sprintkeyz.oxygenate.utils.restartPackage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainConfigScreen(
    isModuleActive: Boolean,
    isRooted: Boolean,
    onNavigateToStatusBar: () -> Unit,
    onNavigateToMisc: () -> Unit,
    onNavigateToKeyguard: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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
                        restartPackage("com.android.systemui")
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

            SectionHeaderItem("Features")
            Spacer(modifier = Modifier.height(8.dp))

            SubmenuItem(
                icon = Icons.Default.SignalCellularAlt,
                title = "Status Bar",
                subtitle = "Status bar tweaks",
                onClick = onNavigateToStatusBar
            )

            Spacer(modifier = Modifier.height(8.dp))

            SubmenuItem(
                icon = Icons.Default.Lock,
                title = "Lock Screen",
                subtitle = "Lock Screen tweaks",
                onClick = onNavigateToKeyguard
            )

            Spacer(modifier = Modifier.height(8.dp))

            SubmenuItem(
                icon = Icons.Default.MiscellaneousServices,
                title = "Misc. Features",
                subtitle = "Tweak various system features",
                onClick = onNavigateToMisc
            )
        }
    }
}