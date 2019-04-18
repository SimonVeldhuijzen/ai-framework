package ai.framework.client.controller

import ai.framework.core.helper.logger
import ai.framework.entity.MoveRequest
import ai.framework.server.repository.UserRepository
import ai.framework.client.players.AiPlayer
import io.javalin.Context
import javax.inject.Inject
import javax.enterprise.inject.Any
import javax.enterprise.inject.Instance

class PlayerController {
    companion object {
        private val logger by logger()
    }

    @Inject @Any
    lateinit var fhirMappers: Instance<AiPlayer>

    private val repository = UserRepository()

    fun move(ctx: Context) {
        val request = ctx.body<MoveRequest>()
    }
}