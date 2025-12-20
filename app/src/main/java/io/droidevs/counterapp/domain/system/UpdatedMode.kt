package io.droidevs.counterapp.domain.system

enum class UpdateMode {
    /** Replace value completely */
    ABSOLUTE,

    /** Add +1 or +n */
    INCREMENT
}
