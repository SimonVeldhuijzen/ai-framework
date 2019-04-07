package ai.framework.game

import ai.framework.core.logger
import ai.framework.entity.MoveResponse
import ai.framework.entity.Player
import java.util.*

class BoterKaasEierenBoard(val players: List<Player>): Board(listOf("row", "column")) {
    companion object {
        private const val NO_PLAYER = -1
        private val logger by logger()
        private val ran = Random()
    }

    private var playerIndex = 0

    val state = Array(3) { IntArray(3) { NO_PLAYER } }

    override fun playerToMove(): Player = players[playerIndex]

    override fun makeMove(move: MoveResponse) {
        val row = move.move["row"] as Int
        val column = move.move["column"] as Int

        state[row][column] = playerIndex
        playerIndex = 1 - playerIndex
    }

    override fun randomMove() {
        val options = LinkedList<Pair<Int, Int>>()

        for (row in 0..2) {
            for (column in 0..2) {
                if (state[row][column] == -1) {
                    options.add(row to column)
                }
            }
        }

        val choice = options[ran.nextInt(options.size)]

        return move(generateRequest().answer(hashMapOf("row" to choice.first, "column" to choice.second)))
    }

    override fun isValid(move: MoveResponse): Boolean {
        if (move.move["row"] !is Int || move.move["column"] !is Int) {
            return false
        }

        val row = move.move["row"] as Int
        val column = move.move["column"] as Int

        return row in 0..2 && column in 0..2 && state[row][column] == NO_PLAYER
    }

    override fun gameFinished(): Boolean {
        if (determineWinner() != null) {
            return true
        }

        return state.all { row -> row.all { cell -> cell != NO_PLAYER } }
    }

    override fun determineWinner(): Player? {
        var winner = checkForWinner(0, 0, 1, 0)
        winner = winner ?: checkForWinner(0, 0, 0, 1)
        winner = winner ?: checkForWinner(0, 0, 1, 1)
        winner = winner ?: checkForWinner(1, 0, 0, 1)
        winner = winner ?: checkForWinner(2, 0, 0, 1)
        winner = winner ?: checkForWinner(0, 1, 1, 0)
        winner = winner ?: checkForWinner(0, 2, 1, 0)
        return winner ?: checkForWinner(0, 2, 1, -1)
    }

    private fun checkForWinner(row: Int, column: Int, rowOffset: Int, columnOffset: Int): Player? {
        if (finished) {
            return winner
        }

        val player = state[row][column]
        if (player == NO_PLAYER) {
            return null
        }

        for (i in 1..2) {
            if (state[row + rowOffset * i][column + columnOffset * i] != player) {
                return null
            }
        }

        return players[player]
    }

    override fun makeCopy(): Board {
        val board = BoterKaasEierenBoard(players)
        board.playerIndex = playerIndex
        for (row in 0..2) {
            for (column in 0..2) {
                board.state[row][column] = state[row][column]
            }
        }

        return board
    }
}