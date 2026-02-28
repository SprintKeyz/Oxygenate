package com.sprintkeyz.oxygenate.data

import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData

// allow scopes

interface IConfigElement<T> {
    val pref: PrefsData<T>
    val scope: String? get() = null
}

class ConfigItem<T>(
    override val pref: PrefsData<T>,
    override val scope: String? = null
) : IConfigElement<T>