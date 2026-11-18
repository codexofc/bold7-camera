package com.light.lightcamera

import android.view.KeyEvent

/**
 * One photo per press of a shutter key: the first down of a press shoots; its repeats, its
 * release, a second down without a release (the same key from two devices) and any other shutter
 * key pressed meanwhile do not. Every event of a shutter key is consumed, so that Android does not
 * act on it as well (a volume change, a fallback).
 */
class ShutterKeys {
    enum class Action { SHOOT, IGNORE, PASS }

    // ponytail: a release lost to a focus change leaves its key here; its next press then only
    // releases it, and the press after shoots.
    private val held = HashSet<Int>()

    fun onKey(keyCode: Int, down: Boolean, repeat: Int): Action {
        if (keyCode !in KEYS) return Action.PASS
        if (!down) {
            held -= keyCode
            return Action.IGNORE
        }
        val first = held.isEmpty() && repeat == 0
        held += keyCode
        return if (first) Action.SHOOT else Action.IGNORE
    }

    private companion object {
        val KEYS = setOf(
            KeyEvent.KEYCODE_DPAD_CENTER, // the trackpad's click (bold7-trackpad sends KEY_SELECT)
            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_NUMPAD_ENTER,
            KeyEvent.KEYCODE_SPACE,
            KeyEvent.KEYCODE_CAMERA,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
        )
    }
}
