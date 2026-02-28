package com.sprintkeyz.oxygenate.data

import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData


object StatusBarConfig {
    val RED_ONE_ALWAYS_IN_STATUS_BAR = ConfigItem(
        pref = PrefsData("red_one_always_in_status_bar", false),
        scope = "com.android.systemui"
    )
}

object MiscDataConst {
    val OPTIMIZED_TOAST_DISABLE = ConfigItem(
        pref = PrefsData("optimized_toast_disable", false),
        scope = "com.oplus.athena"
    )
    val CLOSE_ALL_RECENT_APPS = ConfigItem(
        pref = PrefsData("close_all_recent_apps", false),
        scope = "com.android.launcher"
    )
}

object KeyguardDataConst {
    val KEYGUARD_TIMEOUT = ConfigItem(
        pref = PrefsData("keyguard_timeout", 10000L),
        scope = "system"
    )

    val KEYGUARD_CHARGE_ANIM_EXTENSION = ConfigItem(
        pref = PrefsData("keyguard_charge_anim_extension", 0L),
        scope = "com.android.systemui"
    )
}

object DisplayDataConst {
    val AUTO_BRIGHTNESS_TWEAKS = ConfigItem(
        pref = PrefsData("auto_brightness_tweaks", false),
        scope = "system"
    )

    val AUTO_BRIGHTNESS_MODIFIER = ConfigItem(
        pref = PrefsData("auto_brightness_modifier", 0f)
    )

    val AUTO_BRIGHTNESS_OVERRIDE_TIMER = ConfigItem(
        pref = PrefsData("auto_brightness_override_timer", 0L)
    )
}