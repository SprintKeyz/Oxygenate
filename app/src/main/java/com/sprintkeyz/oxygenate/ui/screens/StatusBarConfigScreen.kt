package com.sprintkeyz.oxygenate.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.highcapable.yukihookapi.hook.factory.prefs
import com.sprintkeyz.oxygenate.data.ModifiedScopesManager
import com.sprintkeyz.oxygenate.data.UIDataConst
import com.sprintkeyz.oxygenate.data.VisibilityState
import com.sprintkeyz.oxygenate.ui.components.SectionHeaderItem
import com.sprintkeyz.oxygenate.ui.components.SwitchItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusBarConfigScreen(
    onBackClick: () -> Unit,
    context: Context
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val ctx = LocalContext.current

    // convert visibilitystate to bool
    var redOneAlwaysVisible by remember {
        mutableStateOf(
            VisibilityState.valueOf(
                context.prefs().get(UIDataConst.HOOK_RED_ONE_VISIBILITY_STATE)
            ) == VisibilityState.ALWAYS
        )
    }

    // can only be always or default
    // if wondering why not a true/false, bug will be fixed eventually trust
    fun onRedOneVisibilityChange(isEnabled: Boolean) {
        // convert bool to visibilitystate
        context.prefs().edit {
            put(UIDataConst.HOOK_RED_ONE_VISIBILITY_STATE,
                if (isEnabled)
                    VisibilityState.ALWAYS.toString()
                else VisibilityState.DEFAULT.toString()
            )
        }

        ModifiedScopesManager.addScope("com.android.systemui", ctx)
        redOneAlwaysVisible = isEnabled
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Status Bar",
                        fontSize = androidx.compose.ui.unit.lerp(
                            34.sp,
                            22.sp,
                            scrollBehavior.state.collapsedFraction
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
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
            SectionHeaderItem("Clock")
            Spacer(modifier = Modifier.height(8.dp))

            SwitchItem(
                icon = Icons.Default.Timer,
                title = "Red 1 Always Visible",
                subtitle = "Make the red 1 in the clock always visible",
                checked = redOneAlwaysVisible,
                onCheckedChange = ::onRedOneVisibilityChange
            )
        }
    }
}