package ai.framework.client.players

import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.board.Move
import ai.framework.core.board.VierOpEenRijBoard
import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType
import java.util.*
import ai.framework.core.helper.logger

class VierOpEenRijSmarterPlayer: AiPlayer<VierOpEenRijBoard>(BoardType.VIER_OP_EEN_RIJ) {
    override fun move(board: VierOpEenRijBoard): Move {
        return checkMoves(board)
    }

    private fun checkMoves(board: VierOpEenRijBoard): Move {
        val possibleMoves = (0 until board.width).filter { i -> board.state[i].last() == PlayerType.NONE }
        val notLosingMoves = LinkedList<Move>()
        for (possibleMove in possibleMoves) {
            val newBoard = board.copy()
            val move = newBoard.createMoveTemplate()
            move.params["column"] = possibleMove
            newBoard.makeMove(move)
            if (newBoard.winner == board.playerToMove) {
                return move
            } else if (newBoard.winner == null) {
                notLosingMoves.add(move)
            }
        }

        return notLosingMoves[notLosingMoves.size / 2]
    }

    override fun copy() = VierOpEenRijSmarterPlayer()
}
