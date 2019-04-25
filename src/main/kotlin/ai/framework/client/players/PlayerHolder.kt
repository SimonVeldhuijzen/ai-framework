package ai.framework.client.players

import ai.framework.core.board.Board
import ai.framework.core.board.Move
import ai.framework.core.constant.BoardType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap

class PlayerHolder {
    companion object {

        val availablePlayers: List<AiPlayer> = listOf(BoterKaasEierenRandomPlayer())

        private val players = HashMap<UUID, AiPlayer>()

        fun makeMove(board: Board, playerUuid: UUID, msPerMove: Int): Move? {
            if (!players.containsKey(playerUuid)) {
                players[playerUuid] = createPlayer(board.type)
            }

            return runBlocking { move(players[playerUuid]!!, board, msPerMove) }
        }

        private suspend fun move(player: AiPlayer, board: Board, msPerMove: Int): Move? {
            var move: Move? = null

            val job = GlobalScope.launch {
                move = player.move(board)
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
            return null
        }

        private fun createPlayer(type: BoardType): AiPlayer {
            return availablePlayers.first { p -> p.type == type }.copy()
        }
    }
}