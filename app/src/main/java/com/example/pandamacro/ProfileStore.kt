package com.example.pandamacro

import android.content.Context
import org.json.JSONArray
import java.io.File

class ProfileStore(context: Context) {
    private val file = File(context.filesDir, "profiles.json")
    fun load(): MutableList<MacroProfile> = runCatching {
        val a = JSONArray(file.readText()); MutableList(a.length()) { i -> MacroProfile.fromJson(a.getJSONObject(i)) }
    }.getOrElse { mutableListOf(MacroProfile("Starter", actions = mutableListOf(MacroAction(ActionType.TAP), MacroAction(ActionType.WAIT, durationMs = 250)))) }
    fun save(profiles: List<MacroProfile>) { val a = JSONArray(); profiles.forEach { a.put(it.toJson()) }; file.writeText(a.toString(2)) }
}
