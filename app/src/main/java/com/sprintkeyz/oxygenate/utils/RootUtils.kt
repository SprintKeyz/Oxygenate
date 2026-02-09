package com.sprintkeyz.oxygenate.utils

import com.topjohnwu.superuser.Shell

// async restart for systemUI
// needing su just for this is a pain, but whatever
// maybe some tweaks will use it later?
fun restartPackage(packageIdentifier: String) {
    Shell.cmd("pkill -f $packageIdentifier").submit()
}

// a good bit faster, but some weird side effects
fun softReboot() {
    Shell.cmd("setprop ctl.restart zygote").submit()
}

fun reboot() {
    Shell.cmd("reboot").submit()
}

fun isRootAvailable(): Boolean {
    return Shell.isAppGrantedRoot() ?: false
}