package io.droidevs.counterapp.ui.message

sealed interface Message {

    data class Text(
        val value: String
    ) : Message

    data class Resource(
        val resId: Int,
        val args: Array<out Any> = emptyArray()
    ) : Message
}