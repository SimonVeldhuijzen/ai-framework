package ai.framework.server.repository

import io.javalin.BadRequestResponse

abstract class Repository {
    fun throwIfEmpty(value: String, name: String) {
        if (value == "") {
            throw BadRequestResponse("No $name given")
        }
    }

    fun throwIfOutOfBounds(value: Int, name: String, min: Int? = null, max: Int? = null) {
        if ((min != null && value < min) || (max != null && value > max)) {
            throw BadRequestResponse("$name should be between ${min ?: "minus infinity"} and ${max ?: "infinity"}. Here: $value")
        }
    }
}