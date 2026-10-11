package com.example.pandamacro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(15, 17, 24); private val panel = Color.rgb(27, 30, 42); private val accent = Color.rgb(139, 92, 246); private val fg = Color.rgb(240, 242, 250)
    private lateinit var store: ProfileStore; private lateinit var profiles: MutableList<MacroProfile>; private var selected = 0; private var repeatCount = 1
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); store = ProfileStore(this); profiles = store.load(); showHome() }
    private fun showHome() {
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(20, 24, 20, 16); setBackgroundColor(bg) }
        root.addView(label("PANDA MACRO", 25, true)); root.addView(label("TOUCH AUTOMATION  •  ANDROID", 11, false).apply { setTextColor(Color.LTGRAY) })
        val scroll = ScrollView(this); val body = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        body.addView(section("PROFILES"))
        profiles.forEachIndexed { i, p -> body.addView(button((if (i == selected) "● " else "") + "${p.name}  ·  ${p.actions.size} actions") { selected = i; repeatCount = p.repeatCount; showHome() }) }
        body.addView(button("+ NEW PROFILE") { profiles += MacroProfile("Profile ${profiles.size + 1}"); selected = profiles.lastIndex; save(); showHome() })
        body.addView(section("ACTION EDITOR"))
        profiles[selected].actions.forEachIndexed { i, a ->
            val desc = when (a.type) { ActionType.TAP -> "TAP  (${a.x}, ${a.y})"; ActionType.WAIT -> "WAIT  ${a.durationMs} ms"; ActionType.SWIPE -> "SWIPE  (${a.x},${a.y}) → (${a.endX},${a.endY})" }
            body.addView(button("${i + 1}. $desc   ✕") { profiles[selected].actions.removeAt(i); save(); showHome() })
        }
        body.addView(button("+ ADD TAP") { profiles[selected].actions += MacroAction(ActionType.TAP); save(); showHome() })
        body.addView(button("+ ADD WAIT") { profiles[selected].actions += MacroAction(ActionType.WAIT, durationMs = 250); save(); showHome() })
        body.addView(button("+ ADD SWIPE") { profiles[selected].actions += MacroAction(ActionType.SWIPE, .7f, .7f, .3f, .7f, 300); save(); showHome() })
        body.addView(section("TIMING & REPEATS")); val timing = label("Repeat count: $repeatCount", 14, true); body.addView(timing)
        body.addView(SeekBar(this).apply { max = 19; progress = (repeatCount - 1).coerceIn(0, 19); setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, fromUser: Boolean) { repeatCount = p + 1; timing.text = "Repeat count: $repeatCount"; profiles[selected].repeatCount = repeatCount }
            override fun onStartTrackingTouch(s: SeekBar?) = Unit
            override fun onStopTrackingTouch(s: SeekBar?) { save() }
        }) })
        body.addView(button("SAVE PROFILE") { save(); Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show() })
        body.addView(section("ANDROID PERMISSIONS"))
        body.addView(button("OPEN ACCESSIBILITY SETTINGS") { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) })
        body.addView(button("FLOATING PANEL PERMISSION") { if (!Settings.canDrawOverlays(this)) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) else Toast.makeText(this, "Overlay permission granted", Toast.LENGTH_SHORT).show() })
        body.addView(label("Macro playback service scaffold is included. Android requires explicit Accessibility permission; some apps may block gestures.", 12, false).apply { setTextColor(Color.LTGRAY); setPadding(2, 16, 2, 20) })
        scroll.addView(body); root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f)); setContentView(root)
    }
    private fun save() { if (profiles.isNotEmpty()) { profiles[selected].repeatCount = repeatCount; store.save(profiles) } }
    private fun label(s: String, size: Int, bold: Boolean) = TextView(this).apply { text = s; textSize = size.toFloat(); setTextColor(fg); if (bold) setTypeface(typeface, Typeface.BOLD); setPadding(4, 8, 4, 8) }
    private fun section(s: String) = label(s, 12, true).apply { setTextColor(Color.rgb(167, 139, 250)); setPadding(2, 14, 2, 8) }
    private fun button(s: String, click: () -> Unit) = Button(this).apply { text = s; isAllCaps = false; setTextColor(fg); background = rounded(panel, 12); setPadding(10, 8, 10, 8); layoutParams = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 7 }; setOnClickListener { click() } }
    private fun rounded(color: Int, radius: Int) = GradientDrawable().apply { setColor(color); cornerRadius = radius.toFloat(); setStroke(1, if (color == panel) Color.rgb(52, 56, 73) else accent) }
}
