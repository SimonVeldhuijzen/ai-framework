package ai.framework.game

import ai.framework.core.logger
import ai.framework.entity.MoveRequest
import ai.framework.entity.MoveResponse
import ai.framework.entity.Player
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
//            logger.warn("Game $uuid: Generating a second request for player ${playerToMove().uuid}")
            return MoveRequest(this, playerToMove(), expectedKeys, optionalKeys, requestUUID!!)
        }

//        logger.info("Game $uuid: Generating request for player ${playerToMove().uuid}")
        val request = MoveRequest(this, playerToMove(), expectedKeys, optionalKeys)
        requestUUID = request.uuid
        return request
    }

    fun move(move: MoveResponse) {
//        logger.info(move.move.toString())

        if (finished) {
//            logger.warn("Game $uuid: Move denied; game finished")
            return
        }

        if (!isCorrectResponse(move)) {
//            logger.warn("Game $uuid: Move denied; incorrect response")
            return
        }

        if (expectedKeysMissing(move) || !isValid(move)) {
//            logger.warn("Game $uuid: Random move; incorrect move")
            randomMove()
            return
        }

//        logger.info("Game $uuid: Moving for player ${move.player.uuid}")
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