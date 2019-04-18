package ai.framework.server.repository

import io.javalin.BadRequestResponse

abstract class Repository {
    fun throwIfEmpty(value: String, name: String) {
        if (value == "") {
            throw BadRequestResponse("No $name given")
        }
    }
}