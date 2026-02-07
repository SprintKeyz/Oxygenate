package com.sprintkeyz.oxygenate.ui.navigation

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sprintkeyz.oxygenate.ui.screens.KeyguardConfigScreen
import com.sprintkeyz.oxygenate.ui.screens.MainConfigScreen
import com.sprintkeyz.oxygenate.ui.screens.MiscConfigScreen
import com.sprintkeyz.oxygenate.ui.screens.StatusBarConfigScreen

@Composable
fun NavHost(
    isModuleActive: Boolean,
    isRooted: Boolean,
    context: Context
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {
        composable("main") {
            MainConfigScreen(
                isModuleActive = isModuleActive,
                isRooted = isRooted,
                onNavigateToStatusBar = { navController.navigate("statusBar") },
                onNavigateToMisc = { navController.navigate("misc") },
                onNavigateToKeyguard = {navController.navigate("keyguard") }
            )
        }

        composable("statusBar") {
            StatusBarConfigScreen(
                onBackClick = { navController.popBackStack() },
                context = context
            )
        }

        composable("misc") {
            MiscConfigScreen(
                onBackClick = { navController.popBackStack() },
                context = context
            )
        }

        composable("keyguard") {
            KeyguardConfigScreen(
                onBackClick = { navController.popBackStack() },
                context = context
            )
        }
    }
}