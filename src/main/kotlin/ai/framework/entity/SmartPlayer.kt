package ai.framework.entity

import ai.framework.game.BoterKaasEierenBoard
import java.util.*

class SmartPlayer(val user: User): Player() {
    override fun name(): String {
        return "Smart"
    }

    override fun makeMove(request: MoveRequest): MoveResponse {
        val options = LinkedList<Pair<Int, Int>>()
        val board: BoterKaasEierenBoard = request.board as BoterKaasEierenBoard

        for (row in 0..2) {
            for (column in 0..2) {
                if (board.state[row][column] == -1) {
                    options.add(row to column)
                }
            }
        }

        for (option in options) {
            val newBoard = board.copy() as BoterKaasEierenBoard
            newBoard.move(newBoard.generateRequest().answer(hashMapOf("row" to option.first, "column" to option.second)))
            if (newBoard.winner == null || newBoard.winner!!.uuid == uuid) {
                return request.answer(hashMapOf("row" to option.first, "column" to option.second))
            }
        }

        return request.answer(hashMapOf("row" to 0, "column" to 0))
    }
}