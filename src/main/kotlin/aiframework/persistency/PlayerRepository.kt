package aiframework.persistency

import aiframework.entity.Player
import java.util.*

class PlayerRepository {
    fun all(): LinkedList<Player> {
        return players
    }

    fun byName(name: String): Player? {
        return players.find { it.name == name }
    }

    fun create(player: Player) {
        if (byName(player.name) == null) {
            players.add(player)
        } else {
            throw Exception("Player with name ${player.name} already exists")
        }
    }

    fun delete(name: String) {
        val player = byName(name)
        if (player != null) {
            players.remove(player)
        }
    }
}