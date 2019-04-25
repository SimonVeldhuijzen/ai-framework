package ai.framework.client.players

import ai.framework.core.board.Board
import ai.framework.core.board.Move
import ai.framework.core.constant.BoardType
import ai.framework.core.helper.logger

abstract class AiPlayer<in T: Board>(val type: BoardType) {
    companion object {
        private val logger by logger()
    }

    fun makeMove(board: T): Move? {
        return try {
            move(board)
        } catch (e: Exception) {
            logger.error("Error making move", e)
            null
        }
    }

    protected abstract fun move(board: T): Move
    abstract fun copy(): AiPlayer<T>
}