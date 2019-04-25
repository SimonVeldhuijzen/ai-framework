package ai.framework.client.players

import ai.framework.core.board.Board
import ai.framework.core.board.Move
import ai.framework.core.constant.BoardType

abstract class AiPlayer(val type: BoardType) {
    abstract fun move(board: Board): Move
    abstract fun copy(): AiPlayer
}