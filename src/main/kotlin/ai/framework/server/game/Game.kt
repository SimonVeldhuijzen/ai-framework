package ai.framework.server.game

import ai.framework.core.board.Board
import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.core.board.Move
import ai.framework.core.constant.GameState
import ai.framework.core.constant.BoardType

class Game(type: BoardType, players: List<Player>) {
    private val board = create(type, players)
    private val playerMapping = board.players.zip(players).toMap()

    var status: GameState = GameState.NOT_INITIALIZED; private set

    fun play() {
        status = GameState.INITIALIZED

        while (!board.finished()) {
            val request = board.requestMove()

            status = GameState.WAITING_FOR_PLAYER
            val move = playerMapping[board.playerToMove]!!.makeMove(request)

            status = GameState.CALCULATING
            if (move == null) {
                board.makeRandomMove()
            } else {
                board.makeMove(move)
            }
        }

        status = GameState.FINISHED
    }

    fun winner(): Player? {
        return if (board.winner == null) {
            null
        } else {
            playerMapping[board.winner!!]
        }
    }

    private fun create(type: BoardType, players: List<Player>): Board {
        return when (type) {
            BoardType.BOTER_KAAS_EIEREN -> BoterKaasEierenBoard(players.size)
        }
    }
}