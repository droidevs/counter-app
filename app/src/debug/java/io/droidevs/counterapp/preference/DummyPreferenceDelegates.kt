package io.droidevs.counterapp.preference

// Factory to create and manage delegates
object DummyPreferenceDelegates {
    private val delegates = mutableMapOf<String, DummyPreferenceDelegate<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrCreate(
        key: String,
        initialValue: T
    ): DummyPreferenceDelegate<T> {
        return delegates.getOrPut(key) {
            DummyPreferenceDelegate(initialValue)
        } as DummyPreferenceDelegate<T>
    }

    fun clearAll() {
        delegates.clear()
    }
}