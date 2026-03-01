package com.sprintkeyz.oxygenate.data

import android.content.Context
import com.highcapable.yukihookapi.hook.factory.prefs
import com.highcapable.yukihookapi.hook.param.PackageParam
import kotlin.time.DurationUnit

/* common prefs functions we do:
- get and set prefs
- get and set prefs as seconds (internally ms)
- get and set prefs as minutes (internally ms)
- get and set prefs as percentages (internally as decimal floats)
- detect if during set pref, a pref
    - a. has a previous value equal to the default value OR but not AND
    - b. has a current value equal to the default value
        - if true, add its scope to modified scopes (if scope is not null)
    - this tells us if it changed to or from the default

- we should add a long press of the apply button to clear modified scopes
- since technically, toggling a switch on and off would add to modified scopes
*/
inline fun <reified T> getPref(
    ctx: Context,
    configItem: ConfigItem<T>,
    durationUnit: DurationUnit? = null,
    percentage: Boolean = false
): T {
    // read the [ref
    val prefs = ctx.prefs()
    val value = prefs.get(configItem.pref)

    if (value !is Number) return value

    // process as number
    val numericValue: Any = when {
        durationUnit == DurationUnit.SECONDS -> {
            value.toLong() / 1000L
        }

        durationUnit == DurationUnit.MINUTES -> {
            value.toLong() / 60000L
        }

        percentage -> {
            value.toFloat() * 100f
        }

        else -> value
    }

    return numericValue as T
}

inline fun <reified T> setPref(
    ctx: Context,
    configItem: ConfigItem<T>,
    value: T,
    durationUnit: DurationUnit? = null,
    percentage: Boolean = false
) {
    val prefs = ctx.prefs()
    val prevVal = getPref(ctx, configItem, durationUnit, percentage)
    val defaultVal = configItem.pref.value

    // get our actual value type as a generic any value (we cast it back later when saving)
    val valToSave: Any = when {
        durationUnit == DurationUnit.SECONDS && value is Number -> {
            value.toLong() * 1000L // convert back to ms
        }

        durationUnit == DurationUnit.MINUTES && value is Number -> {
            value.toLong() * 60000L // convert back to ms
        }

        percentage && value is Number -> {
            value.toFloat() / 100f
        }

        else -> value as Any
    }

    // see if we changed to or from our default
    // if prev was default or new is default we know we changed
    // but if prev was default and new is default we did not
    // we can't just always update on change since sometimes we have a slider
    // where the default slider value disables the hook

    // example
    // old state: false
    // new state: true
    // default: true

    // prevValWasDefault = false
    // newValIsDefault = true
    // update

    // example 2
    // old state: 0
    // new state: 10
    // default: 0

    // prevValWasDefault = true
    // newValIsDefault = false
    // update

    // why do we need xor?
    // say we hold down on a slider, drag it, then release at the old position again
    // if the old position was the default and the new was the default,
    // both would be true and and update would send, but the hook shouldn't change

    val prevValWasDefault = prevVal == defaultVal
    val newValIsDefault = value == defaultVal

    // does this new value mean a modification of the hook's enabled state?
    val modifiesHookEnabledState = prevValWasDefault xor newValIsDefault

    if (modifiesHookEnabledState && configItem.scope != null) {
        ModifiedScopesManager.addScope(configItem.scope, ctx)
    }

    // now we edit our preference...
    prefs.edit {
        put(configItem.pref, valToSave as T)
    }
}

// for hooks
inline fun <reified T> isHookEnabled(param: PackageParam, configItem: ConfigItem<T>): Boolean {
    val default = configItem.pref.value
    val current = param.prefs.get(configItem.pref)
    return current != default
}

inline fun <reified T> getPref(param: PackageParam, configItem: ConfigItem<T>): T {
    return param.prefs.get(configItem.pref)
}