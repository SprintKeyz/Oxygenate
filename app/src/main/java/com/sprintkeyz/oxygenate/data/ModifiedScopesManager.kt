package com.sprintkeyz.oxygenate.data
import android.content.Context

object ModifiedScopesManager {
    private const val KEY_MODIFIED_SCOPES = "modified_scopes"

    fun addScope(scope: String, context: Context) {
        val prefs = VolatilePrefs.getPrefs(context)
        val scopes = getScopes(context).toMutableSet()
        scopes.add(scope)
        prefs.edit().putStringSet(KEY_MODIFIED_SCOPES, scopes).apply()
    }

    fun removeScope(scope: String, context: Context) {
        val prefs = VolatilePrefs.getPrefs(context)
        val scopes = getScopes(context).toMutableSet()
        scopes.remove(scope)
        prefs.edit().putStringSet(KEY_MODIFIED_SCOPES, scopes).apply()
    }

    fun removeAllScopes(context: Context) {
        val prefs = VolatilePrefs.getPrefs(context)
        prefs.edit().putStringSet(KEY_MODIFIED_SCOPES, null).apply()
    }

    fun getScopes(context: Context): Set<String> {
        val prefs = VolatilePrefs.getPrefs(context)
        return prefs.getStringSet(KEY_MODIFIED_SCOPES, null) ?: emptySet()
    }
}