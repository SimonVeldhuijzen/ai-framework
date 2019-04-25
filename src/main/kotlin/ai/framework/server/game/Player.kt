package ai.framework.server.game

import ai.framework.core.board.Board
import ai.framework.core.board.Move
import ai.framework.core.entity.User
import ai.framework.core.helper.logger
import ai.framework.core.traffic.HttpClient
import java.net.SocketTimeoutException
import java.util.*

class Player(val user: User, private val timeout: Int) {
    companion object {
        val logger by logger()
    }

    private val uuid: UUID = UUID.randomUUID()

    private val url = "${user.endpoint}/move?player=$uuid&timeout=$timeout"
    private val client by lazy { HttpClient(timeout, user.endpointCredentials) }

    fun makeMove(board: Board): Move? {
        return try {
            client.get(url, board, Move::class.java)
        } catch (e: Exception) {
            logger.warn("Error calling ${user.name}", e)
            null
        }
    }
}