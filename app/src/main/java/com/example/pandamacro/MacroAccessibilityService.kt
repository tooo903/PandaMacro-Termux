package com.example.pandamacro

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.view.accessibility.AccessibilityEvent

class MacroAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit
    override fun onInterrupt() = Unit

    fun dispatch(action: MacroAction, onComplete: (() -> Unit)? = null) {
        if (action.type == ActionType.WAIT) { Handler(mainLooper).postDelayed({ onComplete?.invoke() }, action.durationMs); return }
        val dm = resources.displayMetrics
        val sx = action.x.coerceIn(0f, 1f) * dm.widthPixels
        val sy = action.y.coerceIn(0f, 1f) * dm.heightPixels
        val path = Path().apply { moveTo(sx, sy); if (action.type == ActionType.SWIPE) lineTo(action.endX.coerceIn(0f, 1f) * dm.widthPixels, action.endY.coerceIn(0f, 1f) * dm.heightPixels) }
        val duration = if (action.type == ActionType.TAP) 60L else action.durationMs.coerceAtLeast(1L)
        dispatchGesture(GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, duration)).build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) { onComplete?.invoke() }
            override fun onCancelled(gestureDescription: GestureDescription?) { onComplete?.invoke() }
        }, null)
    }
}
