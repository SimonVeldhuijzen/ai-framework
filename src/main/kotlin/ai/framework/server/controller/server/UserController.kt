package ai.framework.server.controller.server

import ai.framework.core.helper.cleanStringBody
import ai.framework.core.helper.logger
import ai.framework.core.entity.User
import ai.framework.server.repository.UserRepository
import io.javalin.BadRequestResponse
import io.javalin.Context
import io.javalin.MethodNotAllowedResponse
import io.javalin.apibuilder.CrudHandler
import java.util.*

class UserController: CrudHandler {
    companion object {
        private val logger by logger()
    }

    private val repository = UserRepository()

    override fun create(ctx: Context) {
        val user = ctx.body<User>()
        logger.info("Creating user: ${user.name}")

        val base64 = Base64.getEncoder().encodeToString(user.endpointCredentials.toByteArray())
        ctx.json(repository.create(user.copy(endpointCredentials = base64)))
        ctx.status(201)
    }

    override fun delete(ctx: Context, resourceId: String) {
        logger.info("Deleting user: $resourceId")

        val key = cleanStringBody(ctx.body())
        val user = repository.get(resourceId)
        if (key != user.sharedKey) {
            throw BadRequestResponse("Key not valid")
        }

        repository.delete(resourceId)
    }

    override fun getAll(ctx: Context) {
        logger.info("Getting all users")
        ctx.json(repository.getAll())
    }

    override fun getOne(ctx: Context, resourceId: String) {
        logger.info("Getting user: $resourceId")
        ctx.json(repository.get(resourceId))
    }

    override fun update(ctx: Context, resourceId: String) {
        throw MethodNotAllowedResponse("", HashMap())
    }
}