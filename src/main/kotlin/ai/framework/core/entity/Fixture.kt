package ai.framework.core.entity

import ai.framework.core.constant.BoardType
import ai.framework.core.helper.logger
import ai.framework.core.traffic.Dispatcher
import ai.framework.server.game.Game
import ai.framework.server.game.Player
import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.random.Random

class Fixture(
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val dependent: MutableList<Fixture>,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) var dependentOn: Int,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val users: List<User>,
        private val type: BoardType,
        private val repetitions: Int,
        private val timeout: Int) {
    companion object {
        private val logger by logger()
    }

    private val results = users.map { u: User? -> u to 0 }.toMap().toMutableMap()
    private var winner: User? = null
    var winnerName: String? = null
    val fixtureName: String = users.map { u -> u.name }.joinToString(" vs ")

    fun trigger() {
        dependentOn--
        if (dependentOn <= 0) {
            Dispatcher.add { play() }
        }
    }

    private fun play() {

        val number = Random.nextInt(10000)

        logger.info("Starting fixture $number between ${users.map { u -> u.name }.joinToString(", ")}. $repetitions repetitions with timeout of $timeout")

        results[null] = 0

        val players = users.map { u -> Player(u, timeout) }

        for (i in 1..repetitions) {
            val game = Game(type, players)
            game.play()
            logger.info(game.visualize())
            results[game.winner()?.user] = results[game.winner()?.user]!! + 1
        }

        winner = results.maxBy { u -> u.value }!!.key
        winnerName = winner?.name ?: "None"
        logger.info("Finished fixture $number. Winner: $winner")
        logger.info("Triggering ${dependent.size} fixtures")
        dependent.forEach { f -> f.trigger() }
    }

    override fun toString(): String {
        return "${users.map { u -> u.name }.joinToString(" vs ")}. Dependents: ${dependent.joinToString(" & ") { d -> d.users.map { u -> u.name }.joinToString(" vs ") }}"
    }
}