package ai.framework.core.board

import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType

class VierOpEenRijBoard(playerCount: Int, val width: Int, val height: Int): Board(playerCount, BoardType.VIER_OP_EEN_RIJ) {
    override fun visualize(): String {
        return "\n" + state.joinToString("\n") { s -> s.map { ss -> if (ss == PlayerType.NONE){" "} else {ss.value.toString()} }.joinToString(" | ") }
    }

    private val offsets = listOf(0 to 1, -1 to 1, -1 to 0, -1 to -1)

    val state = Array(width) { Array(height) { PlayerType.NONE } }

    fun copy(): VierOpEenRijBoard {
        val copy = VierOpEenRijBoard(playerCount, width, height)
        copy.winner = winner
        copy.playerToMove = playerToMove
        for (i in 0 until width) {
            for (j in 0 until height) {
                copy.state[i][j] = state[i][j]
            }
        }

        return copy
    }

    override fun isValid(move: Move): Boolean {
        return if (move.checkParam("column", Int::class)) {
            val column = move.getParam<Int>("column")
            column in 0 until width && state[column].last() == PlayerType.NONE
        } else {
            false
        }
    }

    override fun makeRandomMove() {
        val move = createMoveTemplate()
        move.params["column"] = (0 until width).filter { i -> state[i].last() == PlayerType.NONE }.random()
        makeMove(move)
    }

    override fun createMoveTemplate() = Move(mutableMapOf("column" to 0))

    override fun move(move: Move) {
        val column = move.getParam<Int>("column")
        val index = state[column].indexOfFirst { p -> p == PlayerType.NONE }
        state[column][index] = playerToMove
        playerToMove = PlayerType.parse(playerToMove.value % playerCount + 1)
    }

    override fun determineWinner(): PlayerType? {
        for (column in 0 until width) {
            for (row in 0 until height) {
                for (offset in offsets) {
                    val winner = determineWinner(row, column, offset.first, offset.second)
                    if (winner != PlayerType.NONE) {
                        return winner
                    }
                }
            }
        }

        return if (state.all { s -> s.last() != PlayerType.NONE }) {
            PlayerType.NONE
        } else {
            null
        }
    }

    private fun determineWinner(row: Int, column: Int, rowOffset: Int, columnOffset: Int): PlayerType {
        val endRow = row + rowOffset * 3
        val endColumn = column + columnOffset * 3
        if (endRow !in 0 until height || endColumn !in 0 until width) {
            return PlayerType.NONE
        }

        val player = state[column][row]
        if (player == PlayerType.NONE) {
            return player
        }

        for (i in 1..3) {
            if (state[column + columnOffset * i][row + rowOffset * i] != player) {
                return PlayerType.NONE
            }
        }

        return player
    }
}
