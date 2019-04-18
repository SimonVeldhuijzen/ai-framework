package ai.framework.server.game

import ai.framework.core.entity.User
import ai.framework.core.traffic.HttpClient
import ai.framework.entity.MoveRequest
import ai.framework.entity.MoveResponse
import java.util.*

class Player(val user: User) {
    val uuid = UUID.randomUUID()

    private val client by lazy { HttpClient(2000) }

    fun makeMove(request: MoveRequest, timeout: Long): MoveResponse? {
        TODO()
    }
}