package ai.framework.entity

import ai.framework.constant.MessageType
import java.util.*

class Player(val user: User) {
    val uuid = UUID.randomUUID()

    fun makeMove(request: MoveRequest): MoveResponse? {
        user.send("", MessageType.MOVE_REQUEST)
    }
}