package ai.framework.client.controller

import ai.framework.client.players.PlayerHolder
import ai.framework.core.board.Move
import io.javalin.Context
import java.util.*

class PlayerController {
    fun move(ctx: Context) {
        val request = ctx.body<Move>()
        val uuid = UUID.fromString(ctx.queryParam("player"))
        val timeout = ctx.queryParam("timeout", "1")!!.toInt()

        val response = PlayerHolder.makeMove(request, uuid, timeout)
        if (response != null) {
            ctx.json(response)
        }
    }
}