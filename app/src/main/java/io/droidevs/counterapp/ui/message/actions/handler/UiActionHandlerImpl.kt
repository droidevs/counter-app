package io.droidevs.counterapp.ui.message.actions.handler

import io.droidevs.counterapp.ui.message.actions.UiAction
import io.droidevs.counterapp.ui.message.actions.UiActionHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiActionHandlerImpl @Inject constructor(
    // dependencies for handling actions can be injected here
) : UiActionHandler {

    override fun handle(action: UiAction) {
        when (action) {

            is UiAction.UndoDeleteItem -> {
                TODO()
            }

            is UiAction.RetryRequest -> {
                TODO()
            }
        }
    }
}
