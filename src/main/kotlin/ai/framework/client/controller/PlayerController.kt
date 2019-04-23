package ai.framework.client.controller

import ai.framework.core.helper.logger
import ai.framework.server.repository.UserRepository
import ai.framework.client.players.AiPlayer
import ai.framework.client.players.PlayerHolder
import ai.framework.core.board.BoterKaasEierenMove
import ai.framework.core.board.Move
import io.javalin.Context
import java.util.*
import javax.inject.Inject
import javax.enterprise.inject.Any
import javax.enterprise.inject.Instance

class PlayerController {
    @Inject @Any
    lateinit var fhirMappers: Instance<AiPlayer>

    private val repository = UserRepository()

    fun move(ctx: Context) {
        val request = ctx.body<BoterKaasEierenMove>()
        val uuid = UUID.fromString(ctx.queryParam("player"))
        val timeout = ctx.queryParam("timeout", "1")!!.toInt()

        val response = PlayerHolder.makeMove(request, uuid, timeout)
        if (response != null) {
            ctx.json(response)
        }
    }
}