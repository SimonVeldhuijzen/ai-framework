package ai.framework.core.entity

import ai.framework.core.constant.BoardType
import ai.framework.core.helper.logger
import ai.framework.core.traffic.Dispatcher
import ai.framework.server.game.Game
import ai.framework.server.game.Player
import kotlin.random.Random

class Fixture(val dependent: MutableList<Fixture>, var dependentOn: Int, val users: List<User>, private val type: BoardType, private val repetitions: Int, private val timeout: Int) {
    companion object {
        private val logger by logger()
    }

    val results = users.map { u: User? -> u to 0 }.toMap().toMutableMap()
    var winner: User? = null

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
            results[game.winner()?.user] = results[game.winner()?.user]!! + 1
        }

        winner = results.maxBy { u -> u.value }!!.key
        logger.info("Finished fixture $number. Winner: $winner")
        logger.info("Triggering ${dependent.size} fixtures")
        dependent.forEach { f -> f.trigger() }
    }

    override fun toString(): String {
        return "${users.map { u -> u.name }.joinToString(" vs ")}. Dependents: ${dependent.joinToString(" & ") { d -> d.users.map { u -> u.name }.joinToString(" vs ") }}"
    }
}