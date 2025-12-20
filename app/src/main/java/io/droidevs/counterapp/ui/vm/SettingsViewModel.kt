package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.data.SettingKeys
import io.droidevs.counterapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val repo: SettingsRepository
) : ViewModel() {

    /* -------------------- Controls -------------------- */

    private val _soundsOn = MutableStateFlow(repo.getBoolean(SettingKeys.SOUNDS_ON.key, false))

    val soundsOn = _soundsOn
        .asStateFlow()


    private val _vibrationOn = MutableStateFlow(repo.getBoolean(SettingKeys.VIBRATION_ON.key, true))
    val vibrationOn = _vibrationOn
        .asStateFlow()

    private val _hardControlOn = MutableStateFlow(repo.getBoolean(SettingKeys.HARDWARE_BTN_CONTROL_ON.key, false))
    val hardControlOn = _hardControlOn
        .asStateFlow()

    private val _labelControlOn = MutableStateFlow(repo.getBoolean(SettingKeys.LABEL_CONTROL_ON.key, true))
    val labelControlOn = _labelControlOn
        .asStateFlow()

    /* -------------------- Display -------------------- */

    private val _theme = MutableStateFlow(repo.getString(SettingKeys.THEME.key, "system"))
    val theme = _theme
        .asStateFlow()
    private val _keepScreenOn = MutableStateFlow(repo.getBoolean(SettingKeys.KEEP_SCREEN_ON.key, false))
    val keepScreenOn = _keepScreenOn
        .asStateFlow()

    private val _hideControls = MutableStateFlow(repo.getBoolean(SettingKeys.HIDE_CONTROLS.key, false))
    val hideControls = _hideControls
        .asStateFlow()
    private val _hideLastUpdate = MutableStateFlow(repo.getBoolean(SettingKeys.HIDE_LAST_UPDATE.key, false))

    val hideLastUpdate = _hideLastUpdate
        .asStateFlow()

    /* -------------------- Setters -------------------- */

    fun setSoundsOn(value: Boolean) {
        repo.setBoolean(SettingKeys.SOUNDS_ON.key, value)
        _soundsOn.value = value
    }

    fun setVibrationOn(value: Boolean) {
        repo.setBoolean(SettingKeys.VIBRATION_ON.key, value)
        _vibrationOn.value = value
    }

    fun setHardControlOn(value: Boolean) {
        repo.setBoolean(SettingKeys.HARDWARE_BTN_CONTROL_ON.key, value)
        _hardControlOn.value = value
    }

    fun setLabelControlOn(value: Boolean) {
        repo.setBoolean(SettingKeys.LABEL_CONTROL_ON.key, value)
        _labelControlOn.value = value
    }

    fun setTheme(value: String) {
        repo.setString(SettingKeys.THEME.key, value)
        _theme.value = value
        // init theme
    }

    fun setKeepScreenOn(value: Boolean) {
        repo.setBoolean(SettingKeys.KEEP_SCREEN_ON.key, value)
        _keepScreenOn.value = value
    }

    fun setHideControls(value: Boolean) {
        repo.setBoolean(SettingKeys.HIDE_CONTROLS.key, value)
        _hideControls.value = value
    }

    fun setHideLastUpdate(value: Boolean) {
        repo.setBoolean(SettingKeys.HIDE_LAST_UPDATE.key, value)
        _hideLastUpdate.value = value
    }

    /* -------------------- Actions -------------------- */

    fun wipeCounters() {
        // TODO database wipe
    }

    fun exportCounters() {
        // TODO export logic
    }
}

