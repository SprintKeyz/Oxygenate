package com.sprintkeyz.oxygenate.data

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import kotlin.math.abs

// these preferences clear after a reboot! this is for managing scopes to restart.

object VolatilePrefs {
    private const val PREF_NAME = "volatile_scope"
    private const val KEY_BOOT_TIME = "last_boot_time"

    fun getPrefs(context: Context): SharedPreferences {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentBootTime = System.currentTimeMillis() - SystemClock.elapsedRealtime()
        val lastBootTime = prefs.getLong(KEY_BOOT_TIME, 0L)

        // the clock can apparently drift a bit according to people online
        // we can add a 2s margin
        if (abs(currentBootTime - lastBootTime) > 2000) {
            prefs.edit().clear().putLong(KEY_BOOT_TIME, currentBootTime).commit()
        }

        return prefs
    }
}