package ai.framework.client.players

import ai.framework.core.board.BoterKaasEierenMove
import ai.framework.core.board.Move
import ai.framework.core.constant.BoardType
import ai.framework.core.helper.logger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap

class PlayerHolder {
    companion object {
        private val players = HashMap<UUID, AiPlayer>()

        fun makeMove(request: BoterKaasEierenMove, playerUuid: UUID, msPerMove: Int): BoterKaasEierenMove? {

            if (!players.containsKey(playerUuid)) {
                players[playerUuid] = AiPlayer(BoardType.BOTER_KAAS_EIEREN)
            }

            return runBlocking { move(players[playerUuid]!!, request, msPerMove) }
        }

        private suspend fun move(player: AiPlayer, request: BoterKaasEierenMove, msPerMove: Int): BoterKaasEierenMove? {
            var move: BoterKaasEierenMove? = null

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
    }
}