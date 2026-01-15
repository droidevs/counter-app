package io.droidevs.counterapp.ui.message.dispatcher.impl

import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiMessageDispatcherImpl @Inject constructor() : UiMessageDispatcher {

    private val _messages = MutableSharedFlow<UiMessage>(
        extraBufferCapacity = 1
    )

    val messages: SharedFlow<UiMessage> = _messages
        .asSharedFlow()

    override fun flow(): Flow<UiMessage> {
        return messages
    }

    override fun dispatch(message: UiMessage) {
        _messages.tryEmit(message)
    }
}
