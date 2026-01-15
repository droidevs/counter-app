package io.droidevs.counterapp.ui.message.mappers

import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import io.droidevs.counterapp.ui.message.UiMessage

fun UiMessage.Duration.toToastLength() =
    if (this == UiMessage.Duration.SHORT) Toast.LENGTH_SHORT else Toast.LENGTH_LONG

fun UiMessage.Duration.toSnackbarLength() =
    when (this) {
        UiMessage.Duration.SHORT -> Snackbar.LENGTH_SHORT
        UiMessage.Duration.LONG -> Snackbar.LENGTH_LONG
        UiMessage.Duration.INDEFINITE -> Snackbar.LENGTH_INDEFINITE
    }