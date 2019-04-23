package ai.framework.core.entity

import ai.framework.core.constant.BoardType
import ai.framework.core.constant.GameState
import ai.framework.core.helper.logger
import ai.framework.server.game.Player
import ai.framework.server.game.Game
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Tournament(
        val name: String = "Tournament",
        val type: BoardType = BoardType.BOTER_KAAS_EIEREN,
        val repetitions: Int = 100,
        val timeout: Int = 1000,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val key: String = "") {
    companion object {
        private val logger by logger()
    }

    val users = LinkedList<User>()
    var state = GameState.INITIALIZED; private set

    fun start() {
        if (state != GameState.INITIALIZED) {
            return
        }

        val results = users.map { u: User? -> u to 0 }.toMap().toMutableMap()
        results[null] = 0

        state = GameState.CALCULATING

        for (i in 1..repetitions) {
            logger.info("Starting repetition $i of tournament")
            val players = users.map { u -> Player(u, timeout) }
            val game = Game(type, players)
            game.play()
            results[game.winner()?.user] = results[game.winner()?.user]!! + 1
        }

        state = GameState.FINISHED

        logger.info(results.toString())
    }
}
