package ai.framework.core.entity

import ai.framework.core.constant.BoardType
import ai.framework.core.constant.GameState
import ai.framework.core.helper.create
import ai.framework.core.helper.logger
import ai.framework.core.helper.maxPlayers
import ai.framework.core.helper.minPlayers
import ai.framework.core.traffic.Dispatcher
import ai.framework.server.game.Player
import ai.framework.server.game.Game
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.delay
import java.util.*

data class Tournament(
        val name: String,
        val type: BoardType,
        val repetitions: Int,
        val timeout: Int,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val key: String = "") {
    companion object {
        private val logger by logger()
    }

    val users = LinkedList<User>()
    var state = GameState.INITIALIZED; private set
    val fixtures = LinkedList<Fixture>()

    fun start() {
        if (state != GameState.INITIALIZED) {
            return
        }

        state = GameState.CALCULATING

        fixtures.addAll(createFixtures())
        for (fixture in fixtures) {
            logger.info(fixture.toString())
        }
        for (fixture in fixtures.filter { f -> f.dependentOn == 0 }) {
            fixture.trigger()
        }

        logger.info("Created ${fixtures.size} fixtures. Waiting for them now")
    }

    fun createFixtures(): List<Fixture> {
        val min = minPlayers(type)
        val max = maxPlayers(type)

        return if (min == 2 && max == 2) {
            val baseFixtures = LinkedList<Fixture>().toMutableList()
            val progress = LinkedList<Fixture>()
            val done = LinkedList<Fixture>()

            for (i in 0 until users.size) {
                for (j in i + 1 until users.size) {
                    baseFixtures.add(Fixture(LinkedList(), 0, listOf(users[i], users[j]), type, repetitions, timeout))
                }
            }

            baseFixtures.shuffle()

            for (fixture in baseFixtures) {
                val first = progress.firstOrNull { f -> f.users.contains(fixture.users[0]) && f.dependent.none { ff -> ff.users.contains(fixture.users[0]) || ff.users.contains(fixture.users[1]) } }
                val second = progress.firstOrNull { f -> f.users.contains(fixture.users[1]) && f.dependent.none { ff -> ff.users.contains(fixture.users[0]) || ff.users.contains(fixture.users[1]) } }

                if (first != null) {
                    first.dependent.add(fixture)
                    fixture.dependentOn++
                    if (first.dependent.size == 2) {
                        done.add(first)
                        progress.remove(first)
                    }
                }

                if (second != null) {
                    second.dependent.add(fixture)
                    fixture.dependentOn++
                    if (second.dependent.size == 2) {
                        done.add(second)
                        progress.remove(second)
                    }
                }

                progress.add(fixture)
            }

            done.addAll(progress)
            done
        } else {
            LinkedList()
        }
    }
}
