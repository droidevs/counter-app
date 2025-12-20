package io.droidevs.counterapp.ui.listeners

interface VolumeKeyHandler {
    fun onVolumeUp(): Boolean
    fun onVolumeDown(): Boolean
}