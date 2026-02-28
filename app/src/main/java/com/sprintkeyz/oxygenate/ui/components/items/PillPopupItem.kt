package com.sprintkeyz.oxygenate.ui.components.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PillPopupItem(
    message: String,
    buttonText: String? = null,
    isVisible: Boolean,
    onButtonClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(start = (if (buttonText != null) 20.dp else 8.dp), end = 8.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = if (buttonText != null) {
                        Modifier.padding(end = 12.dp)
                    } else {
                        Modifier.padding(12.dp)
                    }
                )

                if (buttonText != null) {
                    Button(
                        onClick = onButtonClick,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.heightIn(min = 32.dp, max = 36.dp)
                    ) {
                        Text(
                            text = buttonText,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

class PillPopupState {
    var isVisible by mutableStateOf(false)
        private set
    var message by mutableStateOf("")
        private set
    var buttonText by mutableStateOf<String?>(null)
        private set
    var onAction by mutableStateOf<() -> Unit>({})
        private set

    fun show(msg: String, actionText: String? = null, action: () -> Unit) {
        message = msg
        buttonText = actionText
        onAction = action
        isVisible = true
    }

    fun dismiss() { isVisible = false }
}

@Composable
fun rememberPillPopupState() = remember { PillPopupState() }

@Composable
fun PillPopupHost(
    state: PillPopupState,
    content: @Composable () -> Unit
) {
    LaunchedEffect(state.isVisible) {
        if (state.isVisible) {
            delay(4000L)
            state.dismiss()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        content()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            PillPopupItem(
                message = state.message,
                buttonText = state.buttonText,
                isVisible = state.isVisible,
                onButtonClick = {
                    state.onAction()
                    state.dismiss()
                }
            )
        }
    }
}