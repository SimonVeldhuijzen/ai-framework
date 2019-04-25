package ai.framework.core.board

import ai.framework.core.constant.BoardType
import ai.framework.core.constant.PlayerType

abstract class Board(val playerCount: Int, val type: BoardType) {
    val players: List<PlayerType> = IntRange(1, playerCount).map { PlayerType.parse(it) }
    var winner: PlayerType? = null; private set
    var playerToMove = PlayerType.PLAYER_ONE; protected set

    fun finished() = winner != null

    fun makeMove(move: Move) {
        if (winner != null) {
            return
        }

        if (isValid(move)) {
            move(move)
        } else {
            makeRandomMove()
        }

        winner = determineWinner()
    }

    abstract fun isValid(move: Move): Boolean
    abstract fun makeRandomMove()
    abstract fun createMoveTemplate(): Move
    protected abstract fun determineWinner(): PlayerType?
    protected abstract fun move(move: Move)
}