package ai.framework.client.players

import ai.framework.core.constant.BoardType

abstract class AiPlayer(val type: BoardType) {
    fun move()
}