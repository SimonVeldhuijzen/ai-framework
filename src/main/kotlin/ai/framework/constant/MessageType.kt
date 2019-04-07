package ai.framework.constant

import ai.framework.entity.MoveRequest
import ai.framework.entity.MoveResponse

enum class MessageType(val value: String, val clazz: Class<*>) {
    CONFIRMATION("Confirmation", String::class.java),
    CREATE_GAME("CreateGame", String::class.java),
    JOIN_GAME("JoinGame", String::class.java),
    START_GAME("StartGame", String::class.java),
    MOVE_REQUEST("MoveRequest", MoveRequest::class.java),
    MOVE_RESPONSE("MoveResponse", MoveResponse::class.java),
    GAME_FINISHED("GameFinished", String::class.java)
}
