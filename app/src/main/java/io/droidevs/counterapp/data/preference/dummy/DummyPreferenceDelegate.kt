package io.droidevs.counterapp.data.preference.dummy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DummyPreferenceDelegate<T>(
    initialValue: T
) {
    private val _flow = MutableStateFlow(initialValue)
    val flow: StateFlow<T> = _flow.asStateFlow()

    suspend fun set(value: T) {
        _flow.value = value
    }

    fun setSync(value: T) {
        _flow.value = value
    }

    fun getCurrent(): T = _flow.value
}