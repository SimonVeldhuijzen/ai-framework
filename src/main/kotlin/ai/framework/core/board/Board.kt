package ai.framework.core.board

import ai.framework.core.constant.PlayerType

abstract class Board(val playerCount: Int) {
    private var lastMoveRequest: Move? = null

    val players: List<PlayerType> = IntRange(1, playerCount).map { PlayerType.parse(it) }
    var winner: PlayerType? = null; private set
    var playerToMove = PlayerType.PLAYER_ONE; protected set

    fun finished() = winner != null

    fun requestMove(): Move {
        if (lastMoveRequest == null) {
            lastMoveRequest = createRequest()
        }

        return lastMoveRequest!!
    }

    fun makeMove(move: Move) {
        if (winner != null) {
            return
        }

        if (isValidMove(move)) {
            move(move)
        } else {
            makeRandomMove()
        }

        winner = determineWinner()
        lastMoveRequest = null
    }

    private fun isValidMove(move: Move) = lastMoveRequest != null && move.uuid == lastMoveRequest!!.uuid && isValid(move)

    abstract fun isValid(move: Move): Boolean
    abstract fun makeRandomMove()
    protected abstract fun createRequest(): Move
    protected abstract fun determineWinner(): PlayerType?
    protected abstract fun move(move: Move)
}