package ai.framework.entity

import ai.framework.game.BoterKaasEierenBoard
import java.util.*

class SlowPlayer(val user: User): Player() {
    companion object {
        private val ran = Random()
    }

    override fun name(): String {
        return "Slow"
    }

    override fun makeMove(request: MoveRequest): MoveResponse {
        Thread.sleep(5000)
        val options = LinkedList<Pair<Int, Int>>()
        val board: BoterKaasEierenBoard = request.board as BoterKaasEierenBoard

        for (row in 0..2) {
            for (column in 0..2) {
                if (board.state[row][column] == -1) {
                    options.add(row to column)
                }
            }
        }

        val choice = options[ran.nextInt(options.size)]

        return request.answer(hashMapOf("row" to choice.first, "column" to choice.second))
    }
}