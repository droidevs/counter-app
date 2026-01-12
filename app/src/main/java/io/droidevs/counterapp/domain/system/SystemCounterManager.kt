package io.droidevs.counterapp.domain.system

interface SystemCounterManager {
    fun fetchSystemCounters(): Map<SystemCounterType, Int>
}
