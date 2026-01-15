package io.droidevs.counterapp.ui.message.dispatcher

import io.droidevs.counterapp.ui.message.UiMessage
import kotlinx.coroutines.flow.Flow

interface UiMessageDispatcher {

    fun flow() : Flow<UiMessage>

    fun dispatch(message: UiMessage)
}
