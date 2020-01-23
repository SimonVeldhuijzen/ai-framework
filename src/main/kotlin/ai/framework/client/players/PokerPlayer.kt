package ai.framework.client.players

import ai.framework.core.board.Move
import ai.framework.core.board.PokerBoard
import ai.framework.core.constant.BoardType
import ai.framework.core.helper.logger

class PokerPlayer: AiPlayer<PokerBoard>(BoardType.POKER) {
    companion object {
        private val logger by logger()
    }

    override fun move(board: PokerBoard): Move {
        return null!!
    }

    override fun copy() = PokerPlayer()
}
