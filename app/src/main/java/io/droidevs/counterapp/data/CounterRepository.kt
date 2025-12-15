package io.droidevs.counterapp.data

import io.droidevs.counterapp.model.Counter
import java.time.Instant

class CounterRepository(var dao: CounterDao) {


    /* -------------------- Read -------------------- */

    suspend fun getAllCounters(): List<Counter> {
        return dao.getLastEdited(5).map { it.toDomain() }
    }

}
