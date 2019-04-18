package ai.framework.core.board

import ai.framework.core.helper.logger
import ai.framework.entity.MoveRequest
import ai.framework.entity.MoveResponse
import ai.framework.server.game.Player
import java.util.*

abstract class Board(val expectedKeys: List<String>, val optionalKeys: List<String> = LinkedList()) {
    companion object {
        private val logger by logger()
    }

    private var requestUUID: UUID? = null

    var uuid: UUID = UUID.randomUUID(); private set
    var finished = false; private set
    var winner: Player? = null; private set

    fun generateRequest() : MoveRequest {
        if (requestUUID != null) {
            return MoveRequest(this, playerToMove(), expectedKeys, optionalKeys, requestUUID!!)
        }

        val request = MoveRequest(this, playerToMove(), expectedKeys, optionalKeys)
        requestUUID = request.uuid
        return request
    }

    fun move(move: MoveResponse) {

        if (finished) {
            return
        }

        if (!isCorrectResponse(move)) {
            return
        }

        if (expectedKeysMissing(move) || !isValid(move)) {
            randomMove()
            return
        }

        makeMove(move)
        requestUUID = null

        if (gameFinished()) {
            winner = determineWinner()
            finished = true
            logger.info("Game $uuid: Finished. Winner: ${winner?.uuid}")
        }
    }

    fun copy(): Board {
        val board = makeCopy()
        board.requestUUID = requestUUID
        board.uuid = uuid
        board.finished = finished
        board.winner = winner
        return board
    }

    private fun expectedKeysMissing(response: MoveResponse): Boolean = !response.move.keys.containsAll(expectedKeys)

    private fun isCorrectResponse(response: MoveResponse): Boolean = response.uuid == requestUUID && response.boardUuid == uuid && response.player.uuid == playerToMove().uuid

    abstract fun playerToMove(): Player
    abstract fun isValid(move: MoveResponse): Boolean
    abstract fun determineWinner(): Player?
    abstract fun gameFinished(): Boolean
    abstract fun randomMove()
    protected abstract fun makeMove(move: MoveResponse)
    protected abstract fun makeCopy(): Board
}