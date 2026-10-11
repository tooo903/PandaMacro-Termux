package com.example.pandamacro

import org.json.JSONArray
import org.json.JSONObject

data class MacroProfile(val name: String, var repeatCount: Int = 1, val actions: MutableList<MacroAction> = mutableListOf()) {
    fun toJson() = JSONObject().apply { put("name", name); put("repeatCount", repeatCount); put("actions", JSONArray().also { a -> actions.forEach { a.put(it.toJson()) } }) }
    companion object {
        fun fromJson(o: JSONObject): MacroProfile {
            val list = mutableListOf<MacroAction>(); val a = o.optJSONArray("actions") ?: JSONArray()
            for (i in 0 until a.length()) a.optJSONObject(i)?.let { list += MacroAction.fromJson(it) }
            return MacroProfile(o.optString("name", "My macro"), o.optInt("repeatCount", 1).coerceIn(1, 999), list)
        }
    }
}
