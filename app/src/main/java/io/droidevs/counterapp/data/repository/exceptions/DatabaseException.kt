package io.droidevs.bmicalc.data.db.exceptions


sealed class DatabaseException : Exception() {

    class NoElementFound : DatabaseException()
    class ConstraintViolated : DatabaseException()
}