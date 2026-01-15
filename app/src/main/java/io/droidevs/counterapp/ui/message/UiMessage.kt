package io.droidevs.counterapp.ui.message

import io.droidevs.counterapp.ui.message.actions.UiAction
import java.util.UUID

sealed interface UiMessage {

    val id: String

    data class Toast(
        override val id: String = UUID.randomUUID().toString(),
        val text: String,
        val duration: Duration = Duration.SHORT
    ) : UiMessage

    data class Snackbar(
        override val id: String = UUID.randomUUID().toString(),
        val text: String,
        val action: Action? = null,
        val duration: Duration = Duration.LONG
    ) : UiMessage {

        data class Action(
            val label: String,
            val uiAction: UiAction
        )
    }

    enum class Duration {
        SHORT, LONG, INDEFINITE
    }
}