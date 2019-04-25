package ai.framework.client.players

import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.board.Move
import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType
import java.util.*
import ai.framework.core.helper.logger

class BoterKaasEierenRandomPlayer: AiPlayer<BoterKaasEierenBoard>(BoardType.BOTER_KAAS_EIEREN) {
    companion object {
        private val logger by logger()
    }

    override fun move(board: BoterKaasEierenBoard): Move {
//        logger.info("Received request for ${board.state}")

        val moves = LinkedList<Pair<Int, Int>>()
        for (row in 0..2) {
            for (column in 0..2) {
                if (board.state[row][column] == PlayerType.NONE) {
                    moves.add(row to column)
                }
            }
        }

        val move = moves[Random().nextInt(moves.size)]
        val result = board.createMoveTemplate()
        result.params["row"] = move.first
        result.params["column"] = move.second

//        logger.info("Response: row = ${move.first}, column = ${move.second}")

        return result
    }

    override fun copy() = BoterKaasEierenRandomPlayer()
}
