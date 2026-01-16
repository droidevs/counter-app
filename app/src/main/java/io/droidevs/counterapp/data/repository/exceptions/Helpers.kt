package io.droidevs.counterapp.data.repository.exceptions

import android.database.CursorWindowAllocationException
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteException
import android.os.Build
import android.os.TransactionTooLargeException
import io.droidevs.bmicalc.data.db.exceptions.DatabaseException
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asResult
import io.droidevs.counterapp.domain.result.asResultAlready
import io.droidevs.counterapp.domain.result.flowRunCatching
import io.droidevs.counterapp.domain.result.runCatchingResult
import io.droidevs.counterapp.domain.result.runCatchingWithResult
import kotlinx.coroutines.flow.Flow


suspend fun <D> runCatchingDatabaseResult(
    block: suspend () -> D
): Result<D, DatabaseError> = runCatchingResult (
    errorTransform = {e->
        e.toDatabaseError()
    }
){
    block()
}

suspend fun <D> runCatchingDatabaseWithResult(
    block: suspend () -> Result<D, DatabaseError>
): Result<D, DatabaseError> = runCatchingWithResult (
    errorTransform = { e ->
        e.toDatabaseError()
    }
){
    block()
}
/**
 * For flows that emit raw values (wraps in Result)
 */
fun <D> Flow<D>.asDatabaseResult(): Flow<Result<D, DatabaseError>> = asResult(
    errorTransform = { e -> e.toDatabaseError() }
)

/**
 * For flows that already emit Result types (just transforms errors)
 */
fun <D> Flow<Result<D, DatabaseError>>.asDatabaseResultAlready(): Flow<Result<D, DatabaseError>> = asResultAlready(
    errorTransform = { e -> e.toDatabaseError() }
)

/**
 * Creates a new flow with database error handling
 */
fun <D> flowRunCatchingDatabase(
    block: suspend () -> Flow<D>
): Flow<Result<D, DatabaseError>> = flowRunCatching(
        errorTransform = { e->
            e.toDatabaseError()
        }
    ){
        block()
    }



fun Throwable.toDatabaseError(): DatabaseError {
    return when (this) {
        is DatabaseException.NoElementFound -> DatabaseError.NoElementFound()
        is SQLiteDatabaseLockedException -> DatabaseError.DatabaseLocked(retryable = true)
        is SQLiteCantOpenDatabaseException -> DatabaseError.DatabaseCorrupted(this)
        is TransactionTooLargeException -> DatabaseError.QueryFailed(this)
        is SQLiteConstraintException -> {
            val constraintType = when {
                message?.contains("UNIQUE") == true -> DatabaseError.ConstraintFailed.ConstraintType.UNIQUE
                message?.contains("NOT NULL") == true -> DatabaseError.ConstraintFailed.ConstraintType.NOT_NULL
                else -> DatabaseError.ConstraintFailed.ConstraintType.FOREIGN_KEY
            }
            DatabaseError.ConstraintFailed(cause = this, constraintType = constraintType)
        }
        is SQLiteException -> DatabaseError.QueryFailed(this)
        is NullPointerException, is IllegalStateException -> throw this // Crash on programmer errors
        else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && this is CursorWindowAllocationException) {
                DatabaseError.QueryFailed(this)
            } else {
                DatabaseError.UnknownError(this)
            }
        }
    }
}