package com.sprintkeyz.oxygenate.hook

import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.param.PackageParam

// abstract class because we love c++

abstract class AbstractHook {
    // this is our open function to call onInit
    // we need our packageparam to read prefs in our hook!
    fun init(param: PackageParam) {
        YLog.debug(isEnabled(param).toString())
        if (!isEnabled(param)) return
        onInit(param)
    }

    // protected functions
    // open means it can be overriden, abstract must be overridden and has no default
    protected open fun isEnabled(param: PackageParam): Boolean = true
    // in case we need our context to get prefs, etc.
    protected abstract fun onInit(param: PackageParam)

    // the use of init and onInit just means every hook doesn't have to
    // re implement an enabled check
}