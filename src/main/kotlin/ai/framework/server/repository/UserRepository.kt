package ai.framework.server.repository

import ai.framework.core.Database
import ai.framework.core.entity.User
import io.javalin.ConflictResponse
import io.javalin.NotFoundResponse

class UserRepository: Repository() {
    fun getAll(): List<User> = Database.users

    fun get(name: String): User = Database.users.find { u -> u.name == name } ?: throw NotFoundResponse("User with name '$name' not found")

    fun delete(name: String) = Database.users.removeIf { u -> u.name == name }

    fun create(user: User): User {
        throwIfEmpty(user.name, "name")
        throwIfEmpty(user.sharedKey, "sharedKey")
        throwIfEmpty(user.endpoint, "endpoint")
        throwIfEmpty(user.endpointCredentials, "endpointCredentials")

        if (Database.users.find { t -> t.name == user.name } != null) {
            throw ConflictResponse("User '${user.name}' already exists")
        }

        Database.users.add(user)
        return user
    }
}