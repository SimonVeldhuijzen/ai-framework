package ai.framework.client.players

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
        private val players = HashMap<UUID, AiPlayer>()

        fun makeMove(request: Move, playerUuid: UUID, msPerMove: Int): Move? {
            if (!players.containsKey(playerUuid)) {
                players[playerUuid] = createPlayer(BoardType.BOTER_KAAS_EIEREN)
            }

            return runBlocking { move(players[playerUuid]!!, request, msPerMove) }
        }

        private suspend fun move(player: AiPlayer, request: Move, msPerMove: Int): Move? {
            var move: Move? = null

            val job = GlobalScope.launch {
                move = player.move(request)
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
            return when (type) {
                BoardType.BOTER_KAAS_EIEREN -> BoterKaasEierenRandomPlayer(type)
            }
        }
    }
}