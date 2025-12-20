package com.sprintkeyz.oxygenate.module

import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication

private const val TAG = "Oxygenate"
class ModuleApp : ModuleApplication() {
    // this is apparently just here to init yukihook stuff?

    override fun onCreate() {
        super.onCreate()
        YLog.debug("Module space started.")
    }
}