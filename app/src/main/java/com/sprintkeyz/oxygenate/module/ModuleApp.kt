package com.sprintkeyz.oxygenate.module

import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication
import com.sprintkeyz.oxygenate.BuildConfig
import com.topjohnwu.superuser.Shell
class ModuleApp : ModuleApplication() {
    // this is apparently just here to init yukihook stuff?

    // oh wait, except now we use it for root
    // this companion object runs before our onCreate
    companion object {
        init {
            Shell.enableVerboseLogging = BuildConfig.DEBUG
            Shell.setDefaultBuilder(Shell.Builder.create()
                .setFlags(Shell.FLAG_MOUNT_MASTER)
                .setTimeout(10)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()

        // trigger a shell so root checks work
        Thread {
            Shell.getShell()
        }.start()

        YLog.debug("Module started.")
    }
}