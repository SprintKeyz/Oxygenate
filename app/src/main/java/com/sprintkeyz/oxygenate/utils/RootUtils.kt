package com.sprintkeyz.oxygenate.utils

import com.topjohnwu.superuser.Shell

// async restart for systemUI
// needing su just for this is a pain, but whatever
// maybe some tweaks will use it later?
fun restartSystemUI() {
    Shell.cmd("pkill -f com.android.systemui").exec()
}

fun isRootAvailable(): Boolean {
    return Shell.isAppGrantedRoot() ?: false
}