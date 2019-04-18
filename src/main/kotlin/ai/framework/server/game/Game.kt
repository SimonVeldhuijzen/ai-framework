package ai.framework.server.game

import ai.framework.core.helper.logger
import ai.framework.entity.MoveRequest
import ai.framework.entity.MoveResponse
import ai.framework.core.constant.GameState
import ai.framework.core.board.Board
import kotlinx.coroutines.*

class Game(val board: Board) {
    companion object {
        private val logger by logger()
    }

    var status: GameState = GameState.NOT_INITIALIZED; private set

    fun play(msPerMove: Long) {
        status = GameState.INITIALIZED

        while (!board.finished) {
            logger.info("Generating request for ${board.playerToMove().user.name}")
            val request = board.generateRequest()

            status = GameState.WAITING_FOR_PLAYER
            logger.info("Asking for move for ${board.playerToMove().user.name}")
            val move = runBlocking { move(request, msPerMove) }

            status = GameState.CALCULATING
            if (move == null) {
                logger.info("Making random move for ${board.playerToMove().user.name}")
                board.randomMove()
            } else {
                logger.info("Making move for ${board.playerToMove().user.name}")
                board.move(move)
            }
        }

        status = GameState.FINISHED
    }

    private suspend fun move(request: MoveRequest, msPerMove: Long): MoveResponse? {
        var move: MoveResponse? = null

        val job = GlobalScope.launch {
            move = request.player.makeMove(request, msPerMove)
        }

        val waitTill = System.currentTimeMillis() + msPerMove
        while (System.currentTimeMillis() < waitTill) {
            delay(10)
            if (move != null) {
                return move
            }
        }

        if (move != null) {
            return move
        }

        job.cancel()
        logger.info("timeout")
        return null
    }
}