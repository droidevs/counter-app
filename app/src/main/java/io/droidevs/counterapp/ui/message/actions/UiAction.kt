package io.droidevs.counterapp.ui.message.actions

sealed interface UiAction {

    data class UndoDeleteItem(
        val itemId: Long
    ) : UiAction

    data class RetryRequest(
        val requestId: String
    ) : UiAction
}
