package io.droidevs.counterapp.ui.hardware

import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import io.droidevs.counterapp.ui.listeners.VolumeKeyHandler

/**
 * Pure dispatcher: given a KeyEvent and the current fragment, decides if it should be consumed.
 * It does not know about preferences; MainActivity should gate calls using HardwareButtonControlManager.
 */
class HardwareButtonKeyDispatcher {

    fun dispatch(event: KeyEvent, fragment: Fragment?): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return false

        Log.i("HardwareButtonKeyDispatcher", "Dispatching key event ${event.keyCode} to $fragment")
        val handler = fragment as? VolumeKeyHandler ?: return false

        Log.i("HardwareButtonKeyDispatcher", "Dispatching key event ${event.keyCode} to $fragment")
        return when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> handler.onVolumeUp()
            KeyEvent.KEYCODE_VOLUME_DOWN -> handler.onVolumeDown()
            else -> false
        }
    }
}

