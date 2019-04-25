package ai.framework.core.board

import ai.framework.core.constant.PlayerType

class BoterKaasEierenBoard(playerCount: Int): Board(playerCount) {
    val state = Array(3) { Array(3) { PlayerType.NONE } }

    override fun move(move: Move) {
        val row = move.getParam<Int>("row")
        val column = move.getParam<Int>("column")
        state[row][column] = playerToMove
        playerToMove = PlayerType.parse(playerToMove.value % playerCount + 1)
    }

    override fun isValid(move: Move): Boolean {
        return if (move.checkParam("row", Int::class) || move.checkParam("column", Int::class)) {
            val row = move.getParam<Int>("row")
            val column = move.getParam<Int>("column")
            row in 0..2 && column in 0..2 && state[row][column] == PlayerType.NONE
        } else {
            false
        }
    }

    override fun makeRandomMove() {
        TODO()
    }

    override fun createRequest() = Move(this, mutableMapOf("row" to 0, "column" to 0))

    override fun determineWinner(): PlayerType? {
        if (finished()) {
            return winner
        }

        val results = ArrayList<PlayerType?>(8)

        results.add(checkForWinner(0, 0, 1, 0))
        results.add(checkForWinner(0, 0, 0, 1))
        results.add(checkForWinner(0, 0, 1, 1))
        results.add(checkForWinner(1, 0, 0, 1))
        results.add(checkForWinner(2, 0, 0, 1))
        results.add(checkForWinner(0, 1, 1, 0))
        results.add(checkForWinner(0, 2, 1, 0))
        results.add(checkForWinner(0, 2, 1, -1))

        return when {
            results.all { r -> r == PlayerType.NONE } -> PlayerType.NONE
            results.any { r -> r != PlayerType.NONE && r != null } -> results.firstOrNull { r -> r != PlayerType.NONE && r != null }
            else -> null
        }
    }

    private fun checkForWinner(row: Int, column: Int, rowOffset: Int, columnOffset: Int): PlayerType? {
        val store = (0..2).map { i -> state[row + rowOffset * i][column + columnOffset * i] }
        if (store.containsAll(players)) {
            return PlayerType.NONE
        }

        if (store.all { p -> p != PlayerType.NONE }) {
            return store[0]
        }

        return null
    }
}
