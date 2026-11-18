package com.light.lightcamera

import android.view.KeyEvent
import com.light.lightcamera.ShutterKeys.Action.IGNORE
import com.light.lightcamera.ShutterKeys.Action.PASS
import com.light.lightcamera.ShutterKeys.Action.SHOOT
import org.junit.Assert.assertEquals
import org.junit.Test

class ShutterKeysTest {
    private val keys = ShutterKeys()

    private fun down(code: Int, repeat: Int = 0) = keys.onKey(code, down = true, repeat = repeat)
    private fun up(code: Int) = keys.onKey(code, down = false, repeat = 0)

    @Test
    fun aPressShootsOnceWhateverItsRepeatsAndRelease() {
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_CAMERA))
        for (r in 1..40) assertEquals(IGNORE, down(KeyEvent.KEYCODE_CAMERA, r))
        assertEquals(IGNORE, up(KeyEvent.KEYCODE_CAMERA))
    }

    @Test
    fun aHeldEnterOrClickShootsOnce() {
        for (code in listOf(KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER)) {
            assertEquals(SHOOT, down(code))
            for (r in 1..40) assertEquals(IGNORE, down(code, r))
            assertEquals(IGNORE, up(code))
        }
    }

    @Test
    fun theNextPressShootsAgain() {
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_ENTER))
        assertEquals(IGNORE, up(KeyEvent.KEYCODE_ENTER))
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_ENTER))
        assertEquals(IGNORE, up(KeyEvent.KEYCODE_ENTER))
    }

    @Test
    fun aSecondDownWithoutReleaseIsTheSamePress() {
        // Two devices sending the same key, or a down replayed: one photo.
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_CAMERA))
        assertEquals(IGNORE, down(KeyEvent.KEYCODE_CAMERA))
        assertEquals(IGNORE, up(KeyEvent.KEYCODE_CAMERA))
        assertEquals(IGNORE, up(KeyEvent.KEYCODE_CAMERA))
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_CAMERA))
    }

    @Test
    fun overlappingShutterKeysShootOnce() {
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_CAMERA))
        assertEquals(IGNORE, down(KeyEvent.KEYCODE_ENTER))
        assertEquals(IGNORE, up(KeyEvent.KEYCODE_CAMERA))
        assertEquals(IGNORE, up(KeyEvent.KEYCODE_ENTER))
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_ENTER))
    }

    @Test
    fun everyShutterKeyShoots() {
        for (code in listOf(
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER,
            KeyEvent.KEYCODE_SPACE, KeyEvent.KEYCODE_CAMERA, KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_VOLUME_DOWN,
        )) {
            assertEquals("down $code", SHOOT, down(code))
            assertEquals("up $code", IGNORE, up(code))
        }
    }

    @Test
    fun otherKeysAreLeftToAndroid() {
        for (code in listOf(KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_FOCUS, KeyEvent.KEYCODE_A)) {
            assertEquals(PASS, down(code))
            assertEquals(PASS, up(code))
        }
        // and they do not hold a shutter press
        assertEquals(PASS, down(KeyEvent.KEYCODE_BACK))
        assertEquals(SHOOT, down(KeyEvent.KEYCODE_ENTER))
    }
}
