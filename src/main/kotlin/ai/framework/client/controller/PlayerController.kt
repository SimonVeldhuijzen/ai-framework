package ai.framework.client.controller

import ai.framework.client.players.PlayerHolder
import ai.framework.core.board.Board
import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.constant.BoardType
import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Context
import java.util.*

class PlayerController {
    fun move(ctx: Context) {
        val board = parseBody(ctx)
        val uuid = UUID.fromString(ctx.queryParam("player"))
        val timeout = ctx.queryParam("timeout", "1")!!.toInt()

        try {
            val response = PlayerHolder.makeMove(board, uuid, timeout)
            if (response != null) {
                ctx.json(response)
            }
        } catch (e: Exception) {
            ctx.status(500)
            ctx.json(e)
        }
    }

    private fun parseBody(ctx: Context): Board {
        return when (extractType(ctx.body())) {
            BoardType.BOTER_KAAS_EIEREN -> ctx.body<BoterKaasEierenBoard>()
        }
    }

    private fun extractType(body: String): BoardType {
        val test = ObjectMapper().readTree(body)
        return BoardType.valueOf(test.get("type").asText())
    }
}
