package ai.framework.client.players

import ai.framework.core.board.Board
import ai.framework.core.board.Move
import ai.framework.core.constant.BoardType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.collections.HashMap
import ai.framework.core.helper.logger
import java.util.UUID

class PlayerHolder {
    companion object {

        val logger by logger()

        val availablePlayers: MutableList<AiPlayer<*>> = mutableListOf(BoterKaasEierenRandomPlayer())

        private val players = HashMap<UUID, AiPlayer<*>>()

        fun<T: Board> makeMove(board: T, playerUuid: UUID, msPerMove: Int): Move? {
            if (!players.containsKey(playerUuid)) {
                players[playerUuid] = createPlayer(board.type)
            }

            val player = players[playerUuid]!! as AiPlayer<T>
            return runBlocking { move(player, board, msPerMove) }
        }

        private suspend fun<T: Board> move(player: AiPlayer<T>, board: T, msPerMove: Int): Move? {
            var move: Move? = null

            val job = GlobalScope.launch {
                move = player.makeMove(board)
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

            logger.info("Timeout. Returning null")

            return null
        }

        private fun createPlayer(type: BoardType): AiPlayer<*> {
            return availablePlayers.first { p -> p.type == type }.copy()
        }
    }
}