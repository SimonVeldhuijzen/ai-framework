package ai.framework.core.entity

import ai.framework.core.constant.BoardType
import ai.framework.core.constant.GameState
import ai.framework.core.helper.logger
import ai.framework.server.game.Player
import ai.framework.core.board.Board
import ai.framework.core.board.BoterKaasEierenBoard
import ai.framework.server.game.Game
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Tournament(val name: String = "Tournament", val type: BoardType = BoardType.BOTER_KAAS_EIEREN, @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val key: String = "") {
    companion object {
        private val logger by logger()
    }

    val users = LinkedList<User>()
    var state: GameState = GameState.INITIALIZED; private set

    fun start() {
        val results = HashMap<User?, Int>()
        users.forEach { u -> results[u] = 0 }
        results[null] = 0

        state = GameState.CALCULATING

        for (i in 0..100) {
            val players = users.map { u -> Player(u) }
            val board = create(type, players)
            val game = Game(board)
            game.play(1000)
            results[game.board.winner?.user] = results[game.board.winner?.user]!! + 1
        }

        state = GameState.FINISHED

        logger.info(results.toString())
    }

    private fun create(type: BoardType, players: List<Player>): Board {
        when (type) {
            BoardType.BOTER_KAAS_EIEREN -> return BoterKaasEierenBoard(players)
        }
    }
}