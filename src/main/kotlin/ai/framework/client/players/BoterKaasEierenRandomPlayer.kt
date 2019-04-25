package ai.framework.client.players

import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.board.Move
import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType
import java.util.*
import ai.framework.core.helper.logger

class BoterKaasEierenRandomPlayer(type: BoardType): AiPlayer(type) {
    companion object {
        private val logger by logger()
    }

    override fun move(request: Move): Move {

        if (request.board !is BoterKaasEierenBoard) {
            throw Exception("")
        }

        logger.info("Received request for ${request.board.state}")

        val moves = LinkedList<Pair<Int, Int>>()
        for (row in 0..2) {
            for (column in 0..2) {
                if (request.board.state[row][column] == PlayerType.NONE) {
                    moves.add(row to column)
                }
            }
        }

        val move = moves[Random().nextInt(moves.size)]
        request.params["row"] = move.first
        request.params["column"] = move.second

        logger.info("Response: row = ${move.first}, column = ${move.second}")

        return request
    }
}