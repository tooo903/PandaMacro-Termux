package com.example.pandamacro

import org.json.JSONObject

enum class ActionType { TAP, WAIT, SWIPE }
data class MacroAction(val type: ActionType, val x: Float = .5f, val y: Float = .5f, val endX: Float = .5f, val endY: Float = .5f, val durationMs: Long = 80L) {
    fun toJson() = JSONObject().apply { put("type", type.name); put("x", x.toDouble()); put("y", y.toDouble()); put("endX", endX.toDouble()); put("endY", endY.toDouble()); put("durationMs", durationMs) }
    companion object {
        fun fromJson(o: JSONObject) = MacroAction(ActionType.valueOf(o.optString("type", "TAP")), o.optDouble("x", .5).toFloat(), o.optDouble("y", .5).toFloat(), o.optDouble("endX", .5).toFloat(), o.optDouble("endY", .5).toFloat(), o.optLong("durationMs", 80).coerceIn(1, 60000))
    }
}
