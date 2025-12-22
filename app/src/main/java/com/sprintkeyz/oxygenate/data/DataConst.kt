package com.sprintkeyz.oxygenate.data

import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData

object UIDataConst {
    val HOOK_RED_ONE_VISIBILITY_STATE = PrefsData("hook_red_one_visibility_state", "DEFAULT")
}

object MiscDataConst {
    val OPTIMIZATION_TOAST_DISABLED_STATE = PrefsData("optimization_toast_disabled_state", false)
}