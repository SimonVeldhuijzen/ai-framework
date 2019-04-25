package ai.framework.server.game

import ai.framework.core.board.Move
import ai.framework.core.entity.User
import ai.framework.core.traffic.HttpClient
import java.net.SocketTimeoutException
import java.util.*

class Player(val user: User, private val timeout: Int) {
    private val uuid: UUID = UUID.randomUUID()

    private val url = "${user.endpoint}/move?player=$uuid&timeout=$timeout"
    private val client by lazy { HttpClient(timeout) }

    fun makeMove(request: Move): Move? {
        return try {
            client.get(url, request, Move::class.java)
        } catch (_: SocketTimeoutException) {
            null
        }
    }
}